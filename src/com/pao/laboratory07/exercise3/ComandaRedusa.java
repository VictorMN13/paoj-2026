package com.pao.laboratory07.exercise3;

public final class ComandaRedusa extends Comanda {
    protected int discount;

    public ComandaRedusa(String nume, double pret, int discount,  String client) {
        super(nume, pret, client);
        this.discount = discount;
    }

    @Override
    public double pretFinal() {
        return pret * (1 - discount / 100.0);
    }

    @Override
    public String descriere() {
        return "DISCOUNTED: " + nume + ", pret: " + String.format("%.2f", pretFinal()) + " lei (-" + discount + "%) [PLACED] - client: " + client;
    }

    @Override
    public String getTip() {
        return "DISCOUNTED";
    }
}
