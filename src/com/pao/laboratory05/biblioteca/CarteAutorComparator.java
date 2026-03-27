package com.pao.laboratory05.biblioteca;

import java.util.Comparator;

public class CarteAutorComparator implements Comparator<Carte> {
    @Override
    public int compare(Carte o1, Carte o2) {
        return o1.getAutor().compareTo(o2.getAutor());
    }
}
