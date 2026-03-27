package com.pao.laboratory05.biblioteca;

import java.util.Arrays;
import java.util.Comparator;

public class BibliotecaService {
    private Carte[] carti = new Carte[0];

    private BibliotecaService() {
    }

    private static class Holder {
        private static BibliotecaService instance = new BibliotecaService();
    }

    static BibliotecaService getInstance() {
        return Holder.instance;
    }

     void addCarte(Carte carte) {
        Carte[] temp = new Carte[carti.length + 1];
        System.arraycopy(carti, 0, temp, 0, carti.length);
        temp[carti.length] = carte;
        carti = temp;
        System.out.println("Cartea " + carte + " a fost adaugata cu succes");
    }

    void listSortedByRating() {
        Carte[] temp = new Carte[carti.length];
        System.arraycopy(carti, 0, temp, 0, carti.length);
        Arrays.sort(temp);
        for  (Carte carte : temp) {
            System.out.println(carte);
        }
    }

     void listSortedBy(Comparator<Carte> comparator) {
        Carte[] temp = new Carte[carti.length];
        System.arraycopy(carti, 0, temp, 0, carti.length);
        Arrays.sort(temp, comparator);
        for (int i = 0; i < temp.length; i++) {
            System.out.println(temp[i]);
        }
    }
}
