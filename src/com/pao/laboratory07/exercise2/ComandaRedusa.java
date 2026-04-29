package com.pao.laboratory07.exercise2;

public final class ComandaRedusa extends Comanda {
    private int discount;

    public ComandaRedusa(String nume, double pret, int discount) {
        super(nume, pret);
        this.discount = discount;
    }

    @Override
    public double pretFinal() {
        return pret * (1 - discount / 100.0);
    }

    @Override
    public String descriere() {
        return "DISCOUNTED: " + nume + ", pret: " + String.format("%.2f", pretFinal()) + " lei (-" + discount + "%) [PLACED]";
    }
}
