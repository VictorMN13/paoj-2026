package com.pao.laboratory07.exercise2;

public final class ComandaGratuita extends Comanda {
    public ComandaGratuita(String nume) {
        super(nume, 0);
    }

    @Override
    public double pretFinal() {
        return 0;
    }

    @Override
    public String descriere() {
        return "GIFT: " + nume + ", gratuit [PLACED]";
    }
}
