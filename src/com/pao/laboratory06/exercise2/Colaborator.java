package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public abstract class Colaborator implements IOperatiiCitireScriere{
    protected String nume;
    protected String prenume;
    protected double venitBrutLunar;

    public abstract double calculeazaVenitNetAnual();
    public abstract TipColaborator getTip();

    public Colaborator() {};

    public Colaborator(String nume, String prenume, double venitBrutLunar) {
        this.nume = nume;
        this.prenume = prenume;
        this.venitBrutLunar = venitBrutLunar;
    }

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitBrutLunar = in.nextDouble();
    }

    @Override
    public void afiseaza() {
        System.out.printf("%s: %s %s, venit net anual: %.2f lei\n",
                tipContract(),
                this.nume,
                this.prenume,
                calculeazaVenitNetAnual());
    }

//    @Override
//    public int compareTo(Colaborator o) {
//        return Double.compare(o.calculeazaVenitNetAnual(), this.calculeazaVenitNetAnual());
//    }

}
