package com.pao.proiect.bank_app;

import com.pao.proiect.bank_app.exception.BancaException;
import com.pao.proiect.bank_app.exception.ContException;
import com.pao.proiect.bank_app.exception.UserException;
import com.pao.proiect.bank_app.model.*;
import com.pao.proiect.bank_app.service.ContService;
import com.pao.proiect.bank_app.service.TranzactieService;
import com.pao.proiect.bank_app.service.UserService;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ContService contService = ContService.getInstance();
    private static final TranzactieService tranzactieService = TranzactieService.getInstance();
    private static final UserService userService = UserService.getInstance();

    public static void main(String[] args) {
        try {
            Manager admin = new Manager("Sef", "Sefulescu", "admin@banca.ro", "admin123", 10000, "Centrala", 2000, "Conducere");
            Angajat ghiseu = new Angajat( "Popa", "Ion", "angajat@banca.ro", "angajat123", 4000, "Sucursala 1");

            userService.adaugaUser(admin);
            userService.adaugaUser(ghiseu);
        } catch (
                UserException e) {
            System.out.println("Eroare la incarcarea angajatilor de test: " + e.getMessage());
        }

        boolean ruleaza = true;
        while (ruleaza) {
            System.out.println("\n=======================================");
            System.out.println("  BUN VENIT LA SISTEMUL BANCAR ");
            System.out.println("=======================================");
            System.out.println("1. Inregistrare Client Nou");
            System.out.println("2. Autentificare (Login)");
            System.out.println("0. Iesire din aplicatie");
            System.out.print("Selectati optiunea: ");

            try {
                int optiune = Integer.parseInt(scanner.nextLine());

                switch (optiune) {
                    case 1 -> meniuInregistrare();
                    case 2 -> meniuAutentificare();
                    case 0 -> {
                        System.out.println("Iesire din sistem. La revedere!");
                        ruleaza = false;
                    }
                    default -> System.out.println("Optiune invalida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Eroare: Va rugam introduceti o cifra valida.");
            }
        }
    }

    private static void meniuInregistrare() {
        System.out.println("\n--- INREGISTRARE CLIENT NOU ---");
        System.out.print("Nume: "); String nume = scanner.nextLine();
        System.out.print("Prenume: "); String prenume = scanner.nextLine();
        System.out.print("Email: "); String email = scanner.nextLine();
        System.out.print("Nr. telefon: "); String nr_telefon = scanner.nextLine();
        System.out.println("Adresa: "); String adresa = scanner.nextLine();
        System.out.print("Alegeti o parola: "); String parola = scanner.nextLine();

        try {
            Client clientNou = new Client(nume, prenume, email, parola, adresa, nr_telefon);
            userService.adaugaUser(clientNou);
            System.out.println("[Sistem] Cont de utilizator creat cu succes! Va puteti autentifica.");
        } catch (Exception e) {
            System.out.println("[Eroare] " + e.getMessage());
        }
    }

    private static void meniuAutentificare() {
        System.out.print("\nEmail: ");
        String email = scanner.nextLine();
        System.out.print("Parola: ");
        String parola = scanner.nextLine();

        if (userService.login(email, parola)) {
            User userCurent = userService.getLoggedUsr();
            if (userCurent instanceof Manager) {
                meniuManager();
            } else if (userCurent instanceof Angajat) {
                meniuAngajat();
            } else {
                meniuClient(userCurent.getEmail());
            }

            userService.logout();
            System.out.println("[Sistem] Delogare cu succes.");
        }
    }

    private static void meniuClient(String email) {
        boolean logat = true;
        while (logat) {
            System.out.println("\n--- PORTAL CLIENT: " + email + " ---");
            System.out.println("1. Vezi conturile mele");
            System.out.println("2. Transfer bancar");
            System.out.println("3. Depunere numerar");
            System.out.println("4. Retragere numerar");
            System.out.println("5. Extras de cont");
            System.out.println("6. Calculeaza averea totala");
            System.out.println("0. Delogare");
            System.out.print("Optiune: ");

            try {
                int opt = Integer.parseInt(scanner.nextLine());
                switch (opt) {
                    case 1 -> contService.afiseazaConturiClient(email);
                    case 2 -> {
                        System.out.print("IBAN Sursa: "); ContBancar contS = alegeCont(email);
                        if (contS == null)
                            throw new ContException("Nu aveti nici un cont deschis");
                        String ibanS = contS.getIban();

                        System.out.print("IBAN Destinatie: "); String ibanD = scanner.nextLine();
                        System.out.print("Suma: "); double suma = Double.parseDouble(scanner.nextLine());
                        tranzactieService.realizeazaTransfer(ibanS, ibanD, suma);
                    }
                    case 3 -> {
                        ContBancar contSelectat = alegeCont(email);
                        if (contSelectat != null) {
                            System.out.print("Suma de depus: ");
                            double suma = Double.parseDouble(scanner.nextLine());

                            tranzactieService.realizeazaDepunere(contSelectat.getIban(), suma);
                            System.out.println("[Sistem] Depunere efectuata cu succes!");
                        }
                    }
                    case 4 -> {
                        ContBancar contSelectat = alegeCont(email);
                        if (contSelectat != null) {
                            System.out.print("Suma de retras: ");
                            double suma = Double.parseDouble(scanner.nextLine());

                            tranzactieService.realizeazaRetragere(contSelectat.getIban(), suma);
                            System.out.println("[Sistem] Retragere efectuata cu succes!");
                        }
                    }
                    case 5 -> {
                        System.out.print("IBAN: "); String iban = scanner.nextLine();
                        System.out.print("Luni in urma (-1 pt tot istoric): "); int luni = Integer.parseInt(scanner.nextLine());
                        tranzactieService.afiseazaExtrasCont(iban, luni);
                    }
                    case 6 -> {
                        System.out.print("Moneda referinta (ex: RON, EUR): "); String moneda = scanner.nextLine();
                        double avere = contService.calculeazaAvereClient(email, moneda);
                        System.out.printf("Avere totala consolidata: %.2f %s\n", avere, moneda.toUpperCase());
                    }
                    case 0 -> logat = false;
                    default -> System.out.println("Optiune invalida.");
                }
            } catch (Exception e) {
                gestioneazaEroare(e);
            }
        }
    }

    private static void meniuAngajat() {
        boolean logat = true;
        while (logat) {
            System.out.println("\n--- TERMINAL ANGAJAT GHIȘEU ---");
            System.out.println("1. Deschide cont nou (In constructie)");
            System.out.println("2. Inchide cont");
            System.out.println("3. Emite card nou");
            System.out.println("4. Blocare URGENTA carduri");
            System.out.println("0. Delogare");
            System.out.print("Optiune: ");

            try {
                int opt = Integer.parseInt(scanner.nextLine());
                switch (opt) {
                    case 1 -> {
                        System.out.println("\n--- DESCHIDERE CONT NOU ---");
                        System.out.print("Email client (titular): ");
                        String emailClient = scanner.nextLine();

                        User titular = userService.cautaUsrEmail(emailClient);
                        if (titular == null) {
                            throw new UserException("Client negasit");
                        }

                        if (!(titular instanceof Client client)) {
                            throw new UserException("Acest email nu apartine unui client");
                        }

                        System.out.print("Tip cont (1-Curent, 2-Economii): ");
                        int tipCont = Integer.parseInt(scanner.nextLine());
                        String ibanNou = contService.genereazaIbanAutomat();
                        System.out.print("Sold initial: ");
                        double soldInitial = Double.parseDouble(scanner.nextLine());
                        System.out.print("Moneda (RON, EUR, USD): ");
                        String monedaStr = scanner.nextLine().toUpperCase();
                        Moneda moneda = Moneda.valueOf(monedaStr);

                        ContBancar contNou = (tipCont == 1)
                                ? new ContCurent(ibanNou, soldInitial, moneda, client, 0)
                                : new ContEconomii(ibanNou, soldInitial, moneda, client, 3.5);

                        contService.adaugaCont(contNou);

                        System.out.println("============================================");
                        System.out.println("[Sistem] Cont deschis cu succes!");
                        System.out.println("[Detalii] IBAN generat automat: " + ibanNou);
                        System.out.println("============================================");
                    }
                    case 2 -> {
                        System.out.print("IBAN de inchis: "); String iban = scanner.nextLine();
                        contService.stergeCont(iban);
                        System.out.println("Cont sters cu succes din sistem.");
                    }
                    case 3 -> {
                        System.out.print("IBAN cont curent: "); String iban = scanner.nextLine();
                        contService.emiteCardCont(iban);
                    }
                    case 4 -> {
                        System.out.print("IBAN cont compromis: "); String iban = scanner.nextLine();
                        contService.blocareDeUrgenta(iban);
                    }
                    case 0 -> logat = false;
                    default -> System.out.println("Optiune invalida.");
                }
            } catch (Exception e) {
                gestioneazaEroare(e);
            }
        }
    }

    private static void meniuManager() {
        boolean logat = true;
        while (logat) {
            System.out.println("\n--- PANOU CONTROL MANAGER ---");
            System.out.println("1. Raport Conturi TOP");
            System.out.println("2. Istoric Tranzactii Banca (Global)");
            System.out.println("3. Procesare Final de Luna (Dobanzi & Comisioane)");
            System.out.println("0. Delogare");
            System.out.print("Optiune: ");

            try {
                int opt = Integer.parseInt(scanner.nextLine());
                switch (opt) {
                    case 1 -> {
                        System.out.print("Prag minim RON: "); double prag = Double.parseDouble(scanner.nextLine());
                        contService.afiseazaRaportTopFonduri(prag);
                    }
                    case 2 -> {
                        System.out.print("Luni in urma (-1 pt tot istoric): "); int luni = Integer.parseInt(scanner.nextLine());
                        tranzactieService.afiseazaTranzactiiBanca(luni);
                    }
                    case 3 -> contService.proceseazaFinalDeLuna();
                    case 0 -> logat = false;
                    default -> System.out.println("Optiune invalida.");
                }
            } catch (Exception e) {
                gestioneazaEroare(e);
            }
        }
    }

    private static void gestioneazaEroare(Exception e) {
        if (e instanceof NumberFormatException) {
            System.out.println("[Eroare de Input] Va rugam introduceti date numerice valide!");
        } else if (e instanceof BancaException || e instanceof IllegalArgumentException) {
            System.out.println("[Eroare Business] " + e.getMessage());
        } else {
            System.out.println("[Eroare Neasteptata] " + e.getMessage());
        }
    }

    private static ContBancar alegeCont(String email) {
        List<ContBancar> lista = contService.obtineConturiClient(email);

        if (lista.isEmpty()) {
            return null;
        }

        System.out.println("\n--- CONTURILE DVS. ---");
        for (int i = 0; i < lista.size(); i++) {
            ContBancar c = lista.get(i);
            System.out.printf("%d. %s | Sold: %.2f %s\n",
                    (i + 1), c.getIban(), c.getSold(), c.getMoneda());
        }

        System.out.print("Selectati numarul contului: ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index >= 0 && index < lista.size()) {
                return lista.get(index);
            }
        } catch (Exception e) {
            gestioneazaEroare(e);
        }

        System.out.println("[Eroare] Selectie invalida.");
        return null;
    }
}
