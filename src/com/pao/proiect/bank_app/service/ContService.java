package com.pao.proiect.bank_app.service;

import com.pao.proiect.bank_app.exception.BancaException;
import com.pao.proiect.bank_app.exception.ContException;
import com.pao.proiect.bank_app.exception.ContInexistentException;
import com.pao.proiect.bank_app.exception.FonduriInsuficienteException;
import com.pao.proiect.bank_app.model.*;
import com.pao.proiect.bank_app.repository.CardRepository;
import com.pao.proiect.bank_app.repository.ContBancarRepository;
import com.pao.proiect.bank_app.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ContService {
    private final ContBancarRepository contRepo;
    private final CardRepository cardRepo;

    private ContService() {
        this.contRepo = new ContBancarRepository();
        this.cardRepo = new CardRepository();
    }

    private static class Holder {
        private static final ContService INSTANCE = new ContService();
    }

    public static ContService getInstance() {
        return Holder.INSTANCE;
    }

    public void adaugaCont(ContBancar cont) throws ContException {
        if (cautaContIban(cont.getIban()) != null) {
            throw new ContException("Eroare: Un cont cu IBAN-ul " + cont.getIban() + " exista deja in sistem!");
        }
        try {
            contRepo.save(cont);
        } catch (SQLException e) {
            System.err.println("Eroare la baza de date: " + e.getMessage());
        }
    }

    public void stergeCont(String iban) throws ContInexistentException {
        ContBancar cont = cautaContIban(iban);
        if (cont == null) {
            throw new ContInexistentException(iban, true);
        }
        try {
            contRepo.delete(iban);
        } catch (SQLException e) {
            System.err.println("Eroare la baza de date: " + e.getMessage());
        }
    }

    public ContBancar cautaContIban(String iban) {
        try {
            return contRepo.findById(iban).orElse(null);
        } catch (SQLException e) {
            System.err.println("Eroare la baza de date: " + e.getMessage());
            return null;
        }
    }

    public void afiseazaToateConturile() {
        System.out.println("\n=== TOATE CONTURILE (GRUPATE PE MONEDA) ===");
        try {
            List<ContBancar> toateConturile = contRepo.findAll();

            if (toateConturile.isEmpty()) {
                System.out.println("Nu exista conturi deschise in banca.");
                return;
            }
            Map<String, List<ContBancar>> conturiGrupate = new HashMap<>();

            for (ContBancar cont : toateConturile) {
                String moneda = cont.getMoneda().toString();
                conturiGrupate.putIfAbsent(moneda, new ArrayList<>());
                conturiGrupate.get(moneda).add(cont);
            }

            for (Map.Entry<String, List<ContBancar>> intrare : conturiGrupate.entrySet()) {
                System.out.println("\n--- Moneda: " + intrare.getKey() + " ---");
                for (ContBancar cont : intrare.getValue()) {
                    System.out.println("  " + cont.toString());
                }
            }

        } catch (Exception e) {
            System.err.println("Eroare la incarcarea conturilor: " + e.getMessage());
        }
        System.out.println("===========================================\n");
    }

    public void emiteCardCont(String iban) throws BancaException {
        ContBancar cont = cautaContIban(iban);

        if (cont == null) {
            throw new ContInexistentException("Contul nu a fost gasit.");
        }

        if (!(cont instanceof ContCurent contCurent)) {
            throw new ContException("Se pot emite carduri strict pentru Conturile Curente!");
        }

        Random rand = new Random();
        String numarCard = "4500" + String.format("%012d", Math.abs(rand.nextLong() % 1000000000000L));
        String cvv = String.format("%03d", rand.nextInt(1000));
        String pin = String.format("%04d", rand.nextInt(10000));

        Card cardNou = new Card(numarCard, iban, cvv, pin, false);
        contCurent.adaugaCard(cardNou);

        try {
            cardRepo.save(cardNou);
        } catch (SQLException e) {
            System.err.println("Eroare la baza de date: " + e.getMessage());
        }

        System.out.println("[Sistem] Card emis cu succes! Numar: " + numarCard + " | PIN: " + pin + " | Asociat contului: " + iban);
    }

    public void proceseazaFinalDeLuna() {
        System.out.println("[Sistem] Se ruleaza procesarea de final de luna...");
        int cntProc = 0;

        try {
            List<ContBancar> conturi = contRepo.findAll();
            for (ContBancar cont : conturi) {
                if (cont instanceof ContEconomii contEconomii) {
                    contEconomii.aplicaDobanda();
                    contRepo.update(contEconomii);
                    cntProc++;
                }
                else if (cont instanceof ContCurent contCurent) {
                    try {
                        contCurent.platesteComAdmin();
                        contRepo.update(contCurent);
                        cntProc++;
                    } catch (FonduriInsuficienteException e) {
                        System.out.println("[Atentie] Contul " + contCurent.getIban() + " nu are fonduri pentru plata comisionului de administrare!");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Eroare la baza de date: " + e.getMessage());
        }
        System.out.println("[Sistem] Procesare finalizata. Au fost actualizate " + cntProc + " conturi.");
    }

    public void blocareDeUrgenta(String iban) throws BancaException {
        ContBancar cont = cautaContIban(iban);
        if (cont == null) {
            throw new ContInexistentException("Contul nu a fost gasit pentru blocare.");
        }

        if (cont instanceof ContCurent contCurent) {
            try {
                List<Card> toateCardurile = cardRepo.findAll();
                List<Card> carduri = new ArrayList<>();
                for (Card c : toateCardurile) {
                    if (c.getIbanAsociat().equals(iban)) {
                        carduri.add(c);
                    }
                }

                if (carduri.isEmpty()) {
                    System.out.println("[Sistem] Contul curent nu are niciun card atasat de blocat.");
                    return;
                }

                for (Card c : carduri) {
                    c.setBlocat(true);
                    cardRepo.update(c);
                }
                System.out.println("[Securitate - Alerta] Au fost blocate " + carduri.size() + " carduri pentru IBAN-ul " + iban);
            } catch (SQLException e) {
                System.err.println("Eroare la baza de date: " + e.getMessage());
            }
        } else {
            System.out.println("[Sistem] Acesta este un cont de economii. Nu detine carduri ce pot fi blocate.");
        }
    }

    public double calculeazaAvereClient(String emailClient, String moneda) throws IllegalArgumentException{
        double avere = 0;
        moneda = moneda.toUpperCase();
        try {
            List<ContBancar> conturi = contRepo.findAll();
            for (ContBancar cont : conturi) {
                if (cont.getTitular().getEmail().equals(emailClient)) {
                    avere += cont.getMoneda().getCursFataDeRon() * cont.getSold();
                }
            }
        } catch (SQLException e) {
            System.err.println("Eroare la baza de date: " + e.getMessage());
        }
        return Moneda.RON.convertesteIn(avere, Moneda.valueOf(moneda));
    }

    public List<ContBancar> obtineConturiEligibile(double pragMinimRon) {
        try {
            List<ContBancar> conturi = contRepo.findAll();
            if (conturi.isEmpty()) {
                return new ArrayList<>();
            }

            return conturi.stream()
                    .filter(cont -> (cont.getMoneda().getCursFataDeRon() * cont.getSold()) >= pragMinimRon)
                    .sorted(new SoldDescComparator())
                    .toList();
        } catch (SQLException e) {
            System.err.println("Eroare la baza de date: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void afiseazaRaportTopFonduri(double pragMinimRon) {
        System.out.println("=== RAPORT MANAGEMENT: CONTURI ELIGIBILE OFERTE SPECIALE (Valoare > " + pragMinimRon + " RON) ===");

        try {
            List<ContBancar> conturi = contRepo.findAll();
            if (conturi.isEmpty()) {
                System.out.println("Nu exista conturi inregistrate in sistem.");
                System.out.println("===========================================================================================");
                return;
            }
        } catch (SQLException e) {
            System.err.println("Eroare la baza de date: " + e.getMessage());
        }

        List<ContBancar> conturiFiltrate = obtineConturiEligibile(pragMinimRon);

        if (conturiFiltrate.isEmpty()) {
            System.out.println("Niciun cont nu depaseste pragul de " + pragMinimRon + " RON.");
        } else {
            for (int i = 0; i < conturiFiltrate.size(); i++) {
                ContBancar cont = conturiFiltrate.get(i);
                double echivalentRon = cont.getMoneda().getCursFataDeRon() * cont.getSold();
                System.out.printf("%d. %s | [Echivalent: %.2f RON]\n", (i + 1), cont, echivalentRon);
            }
        }
        System.out.println("===========================================================================================");
    }

    public List<ContBancar> obtineConturiClient(String emailClient) {
        if (emailClient == null || emailClient.isBlank()) {
            return new ArrayList<>();
        }

        try {
            List<ContBancar> conturi = contRepo.findAll();
            return conturi.stream()
                    .filter(cont -> cont.getTitular().getEmail().equalsIgnoreCase(emailClient))
                    .toList();
        } catch (SQLException e) {
            System.err.println("Eroare la baza de date: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void afiseazaConturiClient(String emailClient) throws ContException {
        List<ContBancar> conturiClient = obtineConturiClient(emailClient);
        if (conturiClient.isEmpty()) {
            throw new ContException("Clientul " + emailClient + " nu are niciun cont deschis in acest moment.");
        }

        System.out.println("=== CONTURILE CLIENTULUI: " + emailClient + " ===");
        for (ContBancar cont : conturiClient) {
            System.out.println(cont);
        }
        System.out.println("==================================================");
    }

    public List<String> obtineTopClientiTransferuri() {
        List<String> topClienti = new ArrayList<>();
        String sql = """
            SELECT u.nume, u.prenume, COUNT(t.id) AS numar_transferuri, SUM(t.suma) AS volum_total
            FROM client u
            JOIN cont_bancar cb ON u.id = cb.client_id
            JOIN tranzactie t ON cb.iban = t.iban_sursa
            WHERE t.tip = 'TRANSFER'
            GROUP BY u.id, u.nume, u.prenume
            ORDER BY volum_total DESC
            LIMIT 5
            """;

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            int loc = 1;
            while (rs.next()) {
                String numeComplet = rs.getString("nume") + " " + rs.getString("prenume");
                int nrTranzactii = rs.getInt("numar_transferuri");
                double volum = rs.getDouble("volum_total");

                String format = String.format("%d. %-25s | %d transferuri | Total trimis: %.2f RON",
                        loc++, numeComplet, nrTranzactii, volum);
                topClienti.add(format);
            }
        } catch (Exception e) {
            System.err.println("Eroare la generarea topului: " + e.getMessage());
        }

        return topClienti;
    }

    public void afiseazaTopClienti() {
        System.out.println("\n=== TOP 5 CLIENTI (DUPA VOLUMUL TRANSFERURILOR) ===");
        List<String> top = obtineTopClientiTransferuri();
        if (top.isEmpty()) {
            System.out.println("Nu exista suficiente date pentru a genera topul.");
        } else {
            for (String linie : top) {
                System.out.println(linie);
            }
        }
        System.out.println("===================================================\n");
    }

    public String genereazaIbanAutomat() {
        Random rand = new Random();
        String codBanca = "PAOB";

        StringBuilder contFinal = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            contFinal.append(rand.nextInt(10));
        }

        return "RO00" + codBanca + contFinal;
    }
}