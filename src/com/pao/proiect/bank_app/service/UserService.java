package com.pao.proiect.bank_app.service;

import com.pao.proiect.bank_app.exception.UserException;
import com.pao.proiect.bank_app.model.Angajat;
import com.pao.proiect.bank_app.model.Manager;
import com.pao.proiect.bank_app.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    private final Map<String, User> users;
    private User loggedUsr;

    private UserService() {
        this.users = new HashMap<>();
        this.loggedUsr = null;
    }

    private static class Holder {
        private static final UserService INSTANCE = new UserService();
    }

    public static UserService getInstance() {
        return Holder.INSTANCE;
    }

    public void adaugaUser(User user) throws UserException {
        if (users.containsKey(user.getEmail())) {
            throw new UserException("Acest email este deja inregistrat: " + user.getEmail()); // adaugare clasa exceptii
        }
        users.put(user.getEmail(), user);
    }

    public void stergeUser(String email) throws UserException {
        if (!users.containsKey(email)) {
            throw new UserException("Nu se poate sterge. Utilizatorul nu exista.");
        }
        users.remove(email);
    }

    public void afiseazaUsers() {
        System.out.println("=== LISTA UTILIZATORILOR ===");
        if (users.isEmpty()) {
            System.out.println("Nu exista niciun utilizator inregistrat in sistem.");
        } else {
            for (User user : users.values()) {
                System.out.println(user.toString());
            }
        }
        System.out.println("===================================");
    }

    public User cautaUsrEmail(String email) {
        User gasit = users.get(email);
        if (gasit != null)
            return gasit;

        int distMin = Integer.MAX_VALUE;

        for (String emailUsr : users.keySet()) {
            int distanta = distLev(email, emailUsr);

            if (distanta <= 4 && distanta < distMin) {
                distMin = distanta;
                gasit = users.get(emailUsr);
            }
        }

        return gasit;
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
        User user = users.get(email);

        if (user == null) {
            System.out.println("[Sistem] Email gresit");
            return false;
        }

        if (!user.getParola().equals(parola)) {
            System.out.println("[Sistem] Parola gresita");
            return false;
        }

        this.loggedUsr = user;
        System.out.println("[Sistem] Logare cu succes!");
        return true;
    }

    public void logout() {
        this.loggedUsr = null;
    }

    public User getLoggedUsr() {
        return loggedUsr;
    }

    public void promoveazaAngajat(String email, String departamentNou, double bonusConducere) throws UserException {
        User user = users.get(email);

        if (user == null || !user.getEmail().equals(email)) {
            throw new UserException("Acest user nu exista: " + email);
        }
        if (user instanceof Manager) {
            throw new UserException("Utilizatorul este deja manager!");
        }

        Manager managerNou = getManager(departamentNou, bonusConducere, user);
        users.put(email, managerNou);
    }

    private static Manager getManager(String departamentNou, double bonusConducere, User user) throws UserException {
        if (!(user instanceof Angajat angajat)) {
            throw new UserException("Doar angajatii pot fi promovati (nu clientii)!");
        }

        return new Manager(
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
