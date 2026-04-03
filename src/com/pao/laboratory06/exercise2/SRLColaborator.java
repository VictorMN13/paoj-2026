package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class SRLColaborator extends PersoanaJuridica {
    private double cheltuieli;

    public SRLColaborator() {}

    public SRLColaborator(String nume, String prenume, double venitBrutLunar) {
        super(nume, prenume, venitBrutLunar);
    }

    @Override
    public TipColaborator getTip() {
        return TipColaborator.SRL;
    }

    @Override
    public void citeste(Scanner in) {
        super.citeste(in);
        this.cheltuieli = in.nextDouble();
    }

    @Override
    public String tipContract() {
        return TipColaborator.SRL.name();
    }

    @Override
    public double calculeazaVenitNetAnual() {
        return (venitBrutLunar - cheltuieli) * 12 * 0.84;
    }
}
