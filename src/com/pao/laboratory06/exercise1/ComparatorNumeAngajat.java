package com.pao.laboratory06.exercise1;

import java.util.Comparator;

public class ComparatorNumeAngajat implements Comparator<Angajat> {
    @Override
    public int compare(Angajat o1, Angajat o2) {
        return String.valueOf(o1.getNume()).compareTo(String.valueOf(o2.getNume()));
    }
}
