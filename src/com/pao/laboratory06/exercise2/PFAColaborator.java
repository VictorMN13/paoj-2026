package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class PFAColaborator extends PersoanaFizica {
    private double cheltuieli;

    public PFAColaborator() {}

    public PFAColaborator(String nume, String prenume, double venitBrutLunar) {
        super(nume, prenume, venitBrutLunar);
    }

    @Override
    public void citeste(Scanner in) {
        super.citeste(in);

        this.cheltuieli = in.nextDouble();
    }

    @Override
    public String tipContract() {
        return TipColaborator.PFA.name();
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double net = (venitBrutLunar - cheltuieli) * 12;
        double imp_venit = 0.1 * net;
        double cass;
        double sal_min_an = 48600;
        if (net < 6 * sal_min_an) {
            cass = 0.1 * 6 * sal_min_an;
        }
        else if (net <= 72 * sal_min_an) {
            cass = 0.1 * net;
        }
        else {
            cass = 0.1 * 72 * sal_min_an;
        }
        double cas = 0;
        if (net >= 12 * sal_min_an && net <= 24 * sal_min_an) {
            cas = 0.25 * 12 * sal_min_an;
        }
        else if (net > 24 * sal_min_an) {
            cas =  0.25 * 24 * sal_min_an;
        }
        return net - imp_venit - cass - cas;
    }

    @Override
    public TipColaborator getTip() {
        return  TipColaborator.PFA;
    }
}
