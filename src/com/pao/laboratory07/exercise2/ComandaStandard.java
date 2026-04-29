package com.pao.laboratory07.exercise2;

public final class ComandaStandard extends Comanda {
    public ComandaStandard(String nume, double pret) {
        super(nume, pret);
    }

    @Override
    public double pretFinal() {
        return pret;
    }

    @Override
    public String descriere() {
        return "STANDARD: " + nume + ", pret: " + String.format("%.2f", pretFinal()) + " lei [PLACED]";
    }
}
