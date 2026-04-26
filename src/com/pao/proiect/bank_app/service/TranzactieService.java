package com.pao.proiect.bank_app.service;

import com.pao.proiect.bank_app.exception.BancaException;
import com.pao.proiect.bank_app.exception.ContInexistentException;
import com.pao.proiect.bank_app.exception.TranzactieInvalidaException;
import com.pao.proiect.bank_app.model.ContBancar;
import com.pao.proiect.bank_app.model.Moneda;
import com.pao.proiect.bank_app.model.TipTranzactie;
import com.pao.proiect.bank_app.model.Tranzactie;

import java.time.LocalDate;
import java.util.*;

public class TranzactieService {
    private final TreeSet<Tranzactie> tranzactii;
    private final Map<String, TreeSet<Tranzactie>> tranzactiiCont;
    private final ContService contService;

    private TranzactieService() {
        this.tranzactii = new TreeSet<>();
        this.tranzactiiCont = new HashMap<>();
        this.contService = ContService.getInstance();
    }

    private static class Holder {
        private static final TranzactieService INSTANCE = new TranzactieService();
    }

    public static TranzactieService getInstance() {
        return Holder.INSTANCE;
    }

    private void inregistreaza(Tranzactie tranzactie) {
        tranzactii.add(tranzactie);
        if (tranzactie.ibanSursa() != null) {
            tranzactiiCont.computeIfAbsent(tranzactie.ibanSursa(), k -> new TreeSet<>()).add(tranzactie);
        }

        if (tranzactie.ibanDestinatie() != null) {
            tranzactiiCont.computeIfAbsent(tranzactie.ibanDestinatie(), k -> new TreeSet<>()).add(tranzactie);
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
        inregistreaza(tranzactie);

        System.out.println("[Tranzactie] Transfer realizat cu succes!");
        System.out.printf("[Detalii] S-au retras %.2f %s si s-au depus %.2f %s.\n",
                suma, contSursa.getMoneda(), sumaConvertita, contDestinatie.getMoneda());
        return tranzactie;
    }

    public Tranzactie realizeazaDepunere(String iban, double suma) throws BancaException {
        ContBancar cont = contService.cautaContIban(iban);
        if (cont == null) throw new ContInexistentException(iban, true);

        cont.depunere(suma);

        Tranzactie tranzactie = new Tranzactie(suma, TipTranzactie.DEPUNERE, null, iban);
        inregistreaza(tranzactie);
        return tranzactie;
    }

    public Tranzactie realizeazaRetragere(String iban, double suma) throws BancaException {
        ContBancar cont = contService.cautaContIban(iban);
        if (cont == null) throw new ContInexistentException(iban, true);

        cont.retragere(suma);

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
        return obtineTranzactiiFiltrate(tranzactii, nrLuni);
    }

    public List<Tranzactie> obtineExtrasCont(String iban, int nrLuni) {
        TreeSet<Tranzactie> istoric = tranzactiiCont.get(iban);
        return obtineTranzactiiFiltrate(istoric, nrLuni);
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

    public void afiseazaTranzactiiBanca(int nrLuni) {
        String titlu = (nrLuni == -1) ? "TRANZACTIILE BANCII"
                : "TRANZACTIILE BANCII (Ultimele " + nrLuni + " luni)";

        List<Tranzactie> rezultate = obtineTranzactiiBanca(nrLuni);
        afiseazaListaTranzactii(rezultate, nrLuni, titlu);
    }

    public void afiseazaExtrasCont(String iban, int nrLuni) {
        String titlu = (nrLuni == -1) ? "EXTRAS DE CONT: " + iban
                : "EXTRAS DE CONT: " + iban + " (Ultimele " + nrLuni + " luni)";

        List<Tranzactie> rezultate = obtineExtrasCont(iban, nrLuni);
        afiseazaListaTranzactii(rezultate, nrLuni, titlu);
    }
}
