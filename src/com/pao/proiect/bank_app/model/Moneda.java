package com.pao.proiect.bank_app.model;

public enum Moneda {
    RON(1.0),
    EUR(4.97),
    USD(4.65),
    GBP(5.80);

    private final double cursFataDeRon;

    Moneda(double cursFataDeRon) {
        this.cursFataDeRon = cursFataDeRon;
    }

    public double getCursFataDeRon() {
        return cursFataDeRon;
    }

    public double convertesteIn(double suma, Moneda monedaDestinatie) {
        double sumaInRon = suma * this.cursFataDeRon;
        return sumaInRon / monedaDestinatie.getCursFataDeRon();
    }
}
