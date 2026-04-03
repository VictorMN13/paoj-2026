package com.pao.laboratory06.exercise2;

import  java.util.Scanner;

public class CIMColaborator extends PersoanaFizica{
    private boolean bonus = false;

    public CIMColaborator(String nume, String prenume, double venitBrutLunar) {
        super(nume, prenume, venitBrutLunar);
    }

    public CIMColaborator() {};

    @Override
    public void citeste(Scanner in) {
        super.citeste(in);

        if (in.hasNext()) {
            String bns = in.next();
            this.bonus = bns.equalsIgnoreCase("DA");
        }
    }

    @Override
    public String tipContract() {
        return TipColaborator.CIM.name();
    }

    @Override
    public boolean areBonus() {
        return this.bonus;
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double net =  this.venitBrutLunar * 12 * 0.55;
        if (this.bonus) {
            net *= 1.1;
        }
        return net;
    }

    @Override
    public TipColaborator getTip() {
        return TipColaborator.CIM;
    }
}
