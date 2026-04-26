package com.pao.proiect.bank_app.service;

import com.pao.proiect.bank_app.exception.BancaException;
import com.pao.proiect.bank_app.exception.ContException;
import com.pao.proiect.bank_app.exception.ContInexistentException;
import com.pao.proiect.bank_app.exception.FonduriInsuficienteException;
import com.pao.proiect.bank_app.model.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ContService {
    private final List<ContBancar> conturi;

    private ContService() {
        this.conturi = new ArrayList<>();
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
        conturi.add(cont);
    }

    public void stergeCont(String iban) throws ContInexistentException {
        ContBancar cont = cautaContIban(iban);
        if (cont == null) {
            throw new ContInexistentException(iban, true);
        }
        conturi.remove(cont);
    }

    public ContBancar cautaContIban(String iban) {
        for (ContBancar c : conturi) {
            if (c.getIban().equals(iban)) {
                return c;
            }
        }
        return null;
    }

    public void afiseazaToateConturile() {
        System.out.println("=== LISTA TUTUROR CONTURILOR ===");
        if (conturi.isEmpty()) {
            System.out.println("Banca nu are niciun cont deschis in acest moment.");
        } else {
            for (ContBancar cont : conturi) {
                System.out.println(cont.toString());
            }
        }
        System.out.println("================================");
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

        System.out.println("[Sistem] Card emis cu succes! Numar: " + numarCard + " | PIN: " + pin + " | Asociat contului: " + iban);
    }

    public void proceseazaFinalDeLuna() {
        System.out.println("[Sistem] Se ruleaza procesarea de final de luna...");
        int cntProc = 0;

        for (ContBancar cont : conturi) {
            if (cont instanceof ContEconomii contEconomii) {
                contEconomii.aplicaDobanda();
                cntProc++;
            }
            else if (cont instanceof ContCurent contCurent) {
                try {
                    contCurent.platesteComAdmin();
                    cntProc++;
                } catch (FonduriInsuficienteException e) {
                    System.out.println("[Atentie] Contul " + contCurent.getIban() + " nu are fonduri pentru plata comisionului de administrare!");
                }
            }
        }
        System.out.println("[Sistem] Procesare finalizata. Au fost actualizate " + cntProc + " conturi.");
    }

    public void blocareDeUrgenta(String iban) throws BancaException {
        ContBancar cont = cautaContIban(iban);
        if (cont == null) {
            throw new ContInexistentException("Contul nu a fost gasit pentru blocare.");
        }

        if (cont instanceof ContCurent contCurent) {
            List<Card> carduri = contCurent.getCarduriAtasate();
            if (carduri.isEmpty()) {
                System.out.println("[Sistem] Contul curent nu are niciun card atasat de blocat.");
                return;
            }

            for (Card c : carduri) {
                c.setBlocat(true);
            }
            System.out.println("[Securitate - Alerta] Au fost blocate " + carduri.size() + " carduri pentru IBAN-ul " + iban);
        } else {
            System.out.println("[Sistem] Acesta este un cont de economii. Nu detine carduri ce pot fi blocate.");
        }
    }

    public double calculeazaAvereClient(String emailClient, String moneda) throws IllegalArgumentException{
        double avere = 0;
        moneda = moneda.toUpperCase();
        for (ContBancar cont : conturi) {
            if (cont.getTitular().getEmail().equals(emailClient)) {
                avere += cont.getMoneda().getCursFataDeRon() * cont.getSold();
            }
        }
        return Moneda.RON.convertesteIn(avere, Moneda.valueOf(moneda));
    }

    public List<ContBancar> obtineConturiEligibile(double pragMinimRon) {
        if (conturi.isEmpty()) {
            return new ArrayList<>();
        }

        return conturi.stream()
                .filter(cont -> (cont.getMoneda().getCursFataDeRon() * cont.getSold()) >= pragMinimRon)
                .sorted(new SoldDescComparator())
                .toList();
    }

    public void afiseazaRaportTopFonduri(double pragMinimRon) {
        System.out.println("=== RAPORT MANAGEMENT: CONTURI ELIGIBILE OFERTE SPECIALE (Valoare > " + pragMinimRon + " RON) ===");

        if (conturi.isEmpty()) {
            System.out.println("Nu exista conturi inregistrate in sistem.");
            System.out.println("===========================================================================================");
            return;
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

        return conturi.stream()
                .filter(cont -> cont.getTitular().getEmail().equalsIgnoreCase(emailClient))
                .toList();
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
