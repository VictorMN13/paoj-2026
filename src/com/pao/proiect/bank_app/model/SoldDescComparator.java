package com.pao.proiect.bank_app.model;

import java.util.Comparator;

public class SoldDescComparator implements Comparator<ContBancar> {
    @Override
    public int compare(ContBancar c1, ContBancar c2) {
        double s1 = c1.getMoneda().getCursFataDeRon() * c1.getSold();
        double s2 = c2.getMoneda().getCursFataDeRon() * c2.getSold();
        return Double.compare(s2, s1);
    }
}
