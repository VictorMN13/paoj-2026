package com.pao.proiect.bank_app.service;

import com.pao.proiect.bank_app.exception.BancaException;
import com.pao.proiect.bank_app.exception.ContInexistentException;
import com.pao.proiect.bank_app.exception.TranzactieInvalidaException;
import com.pao.proiect.bank_app.model.ContBancar;
import com.pao.proiect.bank_app.model.Moneda;
import com.pao.proiect.bank_app.model.TipTranzactie;
import com.pao.proiect.bank_app.model.Tranzactie;
import com.pao.proiect.bank_app.repository.ContBancarRepository;
import com.pao.proiect.bank_app.repository.TranzactieRepository;
import com.pao.proiect.bank_app.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class TranzactieService {
    private final TranzactieRepository tranzactieRepo;
    private final ContBancarRepository contRepo;
    private final ContService contService;

    private TranzactieService() {
        this.tranzactieRepo = new TranzactieRepository();
        this.contRepo = new ContBancarRepository();
        this.contService = ContService.getInstance();
    }

    private static class Holder {
        private static final TranzactieService INSTANCE = new TranzactieService();
    }

    public static TranzactieService getInstance() {
        return Holder.INSTANCE;
    }

    private void inregistreaza(Tranzactie tranzactie) {
        try {
            tranzactieRepo.save(tranzactie);
        } catch (SQLException e) {
            System.err.println("Eroare la baza de date: " + e.getMessage());
        }
    }

    public Tranzactie realizeazaTransfer(String ibanSursa, String ibanDestinatie, double suma) throws BancaException {
        ContBancar contSursa = contService.cautaContIban(ibanSursa);
        ContBancar contDestinatie = contService.cautaContIban(ibanDestinatie);

        if (suma <= 0) {
            throw new TranzactieInvalidaException("Suma transferata trebuie sa fie strict pozitiva!");
        }
        if (ibanSursa.equals(ibanDestinatie)) {
            throw new TranzactieInvalidaException("Nu poti transfera bani catre acelasi cont!");
        }

        if (contSursa == null || contDestinatie == null) {
            throw new ContInexistentException("Unul dintre conturile implicate in transfer nu exista!");
        }

        double comision = 0;
        if (contSursa.getMoneda() != contDestinatie.getMoneda()) {
            comision = suma * 0.01;
        }

        contSursa.retragere(suma +  comision);
        double valoareInRon = suma * contSursa.getMoneda().getCursFataDeRon();
        double sumaConvertita = Moneda.RON.convertesteIn(valoareInRon, contDestinatie.getMoneda());
        contDestinatie.depunere(sumaConvertita);

        Tranzactie tranzactie = new Tranzactie(suma, TipTranzactie.TRANSFER, ibanSursa, ibanDestinatie);

        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            try {
                contRepo.update(contSursa);
                contRepo.update(contDestinatie);
                tranzactieRepo.save(tranzactie);

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new SQLException("Rollback executat: " + e.getMessage());
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (Exception e) {
            contSursa.depunere(suma + comision);
            contDestinatie.retragere(sumaConvertita);
            throw new TranzactieInvalidaException("Transferul a eșuat din cauza bazei de date! " + e.getMessage());
        }

        System.out.println("[Tranzactie] Transfer realizat cu succes!");
        System.out.printf("[Detalii] S-au retras %.2f %s si s-au depus %.2f %s.\n",
                suma, contSursa.getMoneda(), sumaConvertita, contDestinatie.getMoneda());
        return tranzactie;
    }

    public Tranzactie realizeazaDepunere(String iban, double suma) throws BancaException {
        ContBancar cont = contService.cautaContIban(iban);
        if (cont == null) throw new ContInexistentException(iban, true);

        cont.depunere(suma);

        try {
            contRepo.update(cont);
        } catch (SQLException e) {
            System.err.println("Eroare la baza de date: " + e.getMessage());
        }

        Tranzactie tranzactie = new Tranzactie(suma, TipTranzactie.DEPUNERE, null, iban);
        inregistreaza(tranzactie);
        return tranzactie;
    }

    public Tranzactie realizeazaRetragere(String iban, double suma) throws BancaException {
        ContBancar cont = contService.cautaContIban(iban);
        if (cont == null) throw new ContInexistentException(iban, true);

        cont.retragere(suma);

        try {
            contRepo.update(cont);
        } catch (SQLException e) {
            System.err.println("Eroare la baza de date: " + e.getMessage());
        }

        Tranzactie tranzactie = new Tranzactie(suma, TipTranzactie.RETRAGERE, iban, null);
        inregistreaza(tranzactie);
        return tranzactie;
    }

    private List<Tranzactie> obtineTranzactiiFiltrate(Collection<Tranzactie> sursa, int nrLuni) {
        if (sursa == null || sursa.isEmpty() || nrLuni == 0) {
            return new ArrayList<>();
        }

        java.time.LocalDateTime limita = (nrLuni == -1) ? null : java.time.LocalDateTime.now().minusMonths(nrLuni);

        return sursa.stream()
                .filter(t -> limita == null || t.timestamp().isAfter(limita))
                .toList();
    }

    public List<Tranzactie> obtineTranzactiiBanca(int nrLuni) {
        try {
            List<Tranzactie> tranzactii = tranzactieRepo.findAll();
            return obtineTranzactiiFiltrate(tranzactii, nrLuni);
        } catch (SQLException e) {
            System.err.println("Eroare la baza de date: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<String> obtineExtrasCont(String iban, int nrLuni) {
        List<String> extras = new ArrayList<>();

        String sql = """
            SELECT t.data_executie, t.tip_tranzactie, t.suma, 
                   t.iban_sursa, cs.nume AS nume_sursa, cs.prenume AS prenume_sursa,
                   t.iban_destinatie, cd.nume AS nume_dest, cd.prenume AS prenume_dest
            FROM tranzactie t
            LEFT JOIN cont_bancar cb_s ON t.iban_sursa = cb_s.iban
            LEFT JOIN client cs ON cb_s.client_id = cs.id
            LEFT JOIN cont_bancar cb_d ON t.iban_destinatie = cb_d.iban
            LEFT JOIN client cd ON cb_d.client_id = cd.id
            WHERE (t.iban_sursa = ? OR t.iban_destinatie = ?)
            """;

        if (nrLuni != -1) {
            sql += " AND t.data_executie >= ?";
        }
        sql += " ORDER BY t.data_executie DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, iban);
            ps.setString(2, iban);

            if (nrLuni != -1) {
                ps.setString(3, java.time.LocalDateTime.now().minusMonths(nrLuni).toString());
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String tip = rs.getString("tip_tranzactie");
                    double suma = rs.getDouble("suma");
                    String data = rs.getString("data_executie").substring(0, 16);

                    String detalii = "";
                    if (tip.equals("TRANSFER")) {
                        if (iban.equals(rs.getString("iban_sursa"))) {
                            String numeDest = rs.getString("nume_dest") != null ? rs.getString("nume_dest") + " " + rs.getString("prenume_dest") : "Necunoscut";
                            detalii = String.format("[- %.2f RON] Transfer catre: %s (%s)", suma, numeDest, rs.getString("iban_destinatie"));
                        } else {
                            String numeSursa = rs.getString("nume_sursa") != null ? rs.getString("nume_sursa") + " " + rs.getString("prenume_sursa") : "Necunoscut";
                            detalii = String.format("[+ %.2f RON] Primit de la: %s (%s)", suma, numeSursa, rs.getString("iban_sursa"));
                        }
                    } else if (tip.equals("DEPUNERE")) {
                        detalii = String.format("[+ %.2f RON] Depunere numerar", suma);
                    } else if (tip.equals("RETRAGERE")) {
                        detalii = String.format("[- %.2f RON] Retragere numerar", suma);
                    }

                    extras.add(data + " | " + detalii);
                }
            }
        } catch (Exception e) {
            System.err.println("Eroare la generarea extrasului: " + e.getMessage());
        }

        return extras;
    }

    private void afiseazaListaTranzactii(List<Tranzactie> tranzactiiFiltrate, int nrLuni, String titlu) {
        System.out.println("\n=== " + titlu + " ===");
        System.out.println("=== Data Eliberarii: " + LocalDate.now() + " ===");

        if (nrLuni == 0) {
            System.out.println("[Eroare] Numarul de luni 0 este invalid. Introdu -1 pentru datele globale sau un numar pozitiv");
            System.out.println("===========================================");
            return;
        }

        if (tranzactiiFiltrate.isEmpty()) {
            System.out.println("Nu au fost gasite tranzactii in perioada specificata pentru aceasta selectie.");
        } else {
            for (Tranzactie t : tranzactiiFiltrate) {
                System.out.println(t);
            }
            System.out.println("Total tranzactii gasite: " + tranzactiiFiltrate.size());
        }
        System.out.println("===========================================");
    }

    private void afiseazaListaExtras(List<String> liniiFormatate, int nrLuni, String titlu) {
        System.out.println("\n=== " + titlu + " ===");
        System.out.println("=== Data Eliberarii: " + LocalDate.now() + " ===");

        if (nrLuni == 0) {
            System.out.println("[Eroare] Numarul de luni 0 este invalid. Introdu -1 pentru datele globale sau un numar pozitiv");
            System.out.println("===========================================");
            return;
        }

        if (liniiFormatate.isEmpty()) {
            System.out.println("Nu au fost gasite tranzactii in perioada specificata pentru aceasta selectie.");
        } else {
            for (String linie : liniiFormatate) {
                System.out.println(linie);
            }
            System.out.println("Total tranzactii gasite: " + liniiFormatate.size());
        }
        System.out.println("===========================================");
    }

    public void afiseazaTranzactiiBanca(int nrLuni) {
        String titlu = (nrLuni == -1) ? "TRANZACTIILE BANCII"
                : "TRANZACTIILE BANCII (Ultimele " + nrLuni + " luni)";

        List<Tranzactie> rezultate = obtineTranzactiiBanca(nrLuni);
        afiseazaListaTranzactii(rezultate, nrLuni, titlu);
    }

    public void afiseazaExtrasCont(String iban, int nrLuni) {
        String titlu = (nrLuni == -1) ? "EXTRAS DE CONT: " + iban
                : "EXTRAS DE CONT: " + iban + " (Ultimele " + nrLuni + " luni)";

        List<String> rezultate = obtineExtrasCont(iban, nrLuni);
        afiseazaListaExtras(rezultate, nrLuni, titlu);
    }

    public List<String> obtineContacteFrecvente(String ibanClient) {
        List<String> contacte = new ArrayList<>();

        String sql = """
            SELECT cd.nume, cd.prenume, cb.iban, COUNT(t.id_tranzactie) AS frecventa, SUM(t.suma) AS total_trimis
            FROM tranzactie t
            JOIN cont_bancar cb ON t.iban_destinatie = cb.iban
            JOIN client cd ON cb.client_id = cd.id
            WHERE t.iban_sursa = ? AND t.tip_tranzactie = 'TRANSFER'
            GROUP BY cd.id, cd.nume, cd.prenume, cb.iban
            ORDER BY frecventa DESC, total_trimis DESC
            LIMIT 5
            """;

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ibanClient);

            try (ResultSet rs = ps.executeQuery()) {
                int index = 1;
                while (rs.next()) {
                    String numeDestinatar = rs.getString("nume") + " " + rs.getString("prenume");
                    String ibanDestinatie = rs.getString("iban");
                    int frecventa = rs.getInt("frecventa");
                    double totalTrimis = rs.getDouble("total_trimis");

                    String format = String.format("%d. %-20s (%s) | %d transferuri anterioare | Total trimis: %.2f RON",
                            index++, numeDestinatar, ibanDestinatie, frecventa, totalTrimis);
                    contacte.add(format);
                }
            }
        } catch (Exception e) {
            System.err.println("Eroare la obținerea contactelor frecvente: " + e.getMessage());
        }

        return contacte;
    }

    public void afiseazaContacteFrecvente(String ibanClient) {
        System.out.println("\n=== SUGESTII TRANSFER: CONTACTE FRECVENTE ===");
        List<String> contacte = obtineContacteFrecvente(ibanClient);

        if (contacte.isEmpty()) {
            System.out.println("Nu ai mai realizat transferuri catre alte conturi din banca.");
        } else {
            System.out.println("Persoanele carora le-ai trimis bani cel mai des:");
            System.out.println("-------------------------------------------------------------------------");
            for (String contact : contacte) {
                System.out.println(contact);
            }
            System.out.println("-------------------------------------------------------------------------");
        }
        System.out.println("===============================================\n");
    }
}