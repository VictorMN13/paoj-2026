package com.pao.laboratory07.exercise3;

public final class ComandaGratuita extends Comanda {
    public ComandaGratuita(String nume, String client) {
        super(nume, 0, client);
    }

    @Override
    public double pretFinal() {
        return 0;
    }

    @Override
    public String descriere() {
        return "GIFT: " + nume + ", gratuit [PLACED] - client: " + client;
    }

    @Override
    public String getTip() {
        return "GIFT";
    }
}
