package com.pao.proiect.bank_app.service;

import com.pao.proiect.bank_app.exception.UserException;
import com.pao.proiect.bank_app.model.Angajat;
import com.pao.proiect.bank_app.model.Client;
import com.pao.proiect.bank_app.model.Manager;
import com.pao.proiect.bank_app.model.User;
import com.pao.proiect.bank_app.repository.AngajatRepository;
import com.pao.proiect.bank_app.repository.ClientRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final ClientRepository clientRepo;
    private final AngajatRepository angajatRepo;

    private User loggedUsr;

    private UserService() {
        this.clientRepo = new ClientRepository();
        this.angajatRepo = new AngajatRepository();
        this.loggedUsr = null;
    }

    private static class Holder {
        private static final UserService INSTANCE = new UserService();
    }

    public static UserService getInstance() {
        return Holder.INSTANCE;
    }

    private List<User> extrageTotiUserii() throws SQLException {
        List<User> totiUserii = new ArrayList<>();
        totiUserii.addAll(clientRepo.findAll());
        totiUserii.addAll(angajatRepo.findAll());
        return totiUserii;
    }

    private User cautaExactEmail(String email) throws SQLException {
        for (User u : extrageTotiUserii()) {
            if (u.getEmail().equals(email)) {
                return u;
            }
        }
        return null;
    }

    public void adaugaUser(User user) throws UserException {
        try {
            if (cautaExactEmail(user.getEmail()) != null) {
                throw new UserException("Acest email este deja inregistrat: " + user.getEmail());
            }

            if (user instanceof Client client) {
                clientRepo.save(client);
            } else if (user instanceof Angajat angajat) {
                angajatRepo.save(angajat);
            }
        } catch (SQLException e) {
            throw new UserException("Eroare la salvarea in baza de date: " + e.getMessage());
        }
    }

    public void stergeUser(String email) throws UserException {
        try {
            User deSters = cautaExactEmail(email);
            if (deSters == null) {
                throw new UserException("Nu se poate sterge. Utilizatorul nu exista.");
            }

            if (deSters instanceof Client) {
                clientRepo.delete(deSters.getId());
            } else if (deSters instanceof Angajat) {
                angajatRepo.delete(deSters.getId());
            }
        } catch (SQLException e) {
            throw new UserException("Eroare la stergerea din baza de date: " + e.getMessage());
        }
    }

    public void afiseazaUsers() {
        System.out.println("=== LISTA UTILIZATORILOR ===");
        try {
            List<User> users = extrageTotiUserii();
            if (users.isEmpty()) {
                System.out.println("Nu exista niciun utilizator inregistrat in sistem.");
            } else {
                for (User user : users) {
                    System.out.println(user.toString());
                }
            }
        } catch (SQLException e) {
            System.out.println("Eroare la citirea din baza de date: " + e.getMessage());
        }
        System.out.println("===================================");
    }

    public User cautaUsrEmail(String email) {
        try {
            User gasit = cautaExactEmail(email);
            if (gasit != null) return gasit;

            int distMin = Integer.MAX_VALUE;
            List<User> users = extrageTotiUserii();

            for (User u : users) {
                int distanta = distLev(email, u.getEmail());
                if (distanta <= 4 && distanta < distMin) {
                    distMin = distanta;
                    gasit = u;
                }
            }
            return gasit;
        } catch (SQLException e) {
            System.out.println("Eroare la cautarea in DB: " + e.getMessage());
            return null;
        }
    }

    private int distLev(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                    dp[i][j] = Math.min(
                            Math.min(dp[i - 1][j] + 1,
                                    dp[i][j - 1] + 1),
                            dp[i - 1][j - 1] + cost
                    );
                }
            }
        }
        return dp[s1.length()][s2.length()];
    }

    public boolean login(String email, String parola) {
        try {
            User user = cautaExactEmail(email);

            if (user == null) {
                System.out.println("[Sistem] Email gresit");
                return false;
            }

            if (!user.getParola().equals(parola)) {
                System.out.println("[Sistem] Parola gresita");
                return false;
            }

            this.loggedUsr = user;
            System.out.println("[Sistem] Logare cu succes ca " + user.getRol() + "!");
            return true;
        } catch (SQLException e) {
            System.out.println("[Sistem] Eroare tehnica la login: " + e.getMessage());
            return false;
        }
    }

    public void logout() {
        this.loggedUsr = null;
        System.out.println("[Sistem] Delogare cu succes!");
    }

    public User getLoggedUsr() {
        return loggedUsr;
    }

    public void promoveazaAngajat(String email, String departamentNou, double bonusConducere) throws UserException {
        try {
            User user = cautaExactEmail(email);

            if (user == null) {
                throw new UserException("Acest user nu exista: " + email);
            }
            if (user instanceof Manager) {
                throw new UserException("Utilizatorul este deja manager!");
            }

            Manager managerNou = getManager(departamentNou, bonusConducere, user);

            angajatRepo.update(managerNou);

            if (loggedUsr != null && loggedUsr.getId().equals(managerNou.getId())) {
                loggedUsr = managerNou;
            }

        } catch (SQLException e) {
            throw new UserException("Eroare la actualizarea in DB: " + e.getMessage());
        }
    }

    private static Manager getManager(String departamentNou, double bonusConducere, User user) throws UserException {
        if (!(user instanceof Angajat angajat)) {
            throw new UserException("Doar angajatii pot fi promovati (nu clientii)!");
        }

        return new Manager(
                angajat.getId(),
                angajat.getNume(),
                angajat.getPrenume(),
                angajat.getEmail(),
                angajat.getParola(),
                angajat.getSalariu(),
                angajat.getSucursala(),
                bonusConducere,
                departamentNou
        );
    }
}