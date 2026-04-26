package com.pao.proiect.bank_app.model;

import java.util.Objects;

public abstract class ContBancar implements OperatiuneBancara {
    protected final String iban;
    protected double sold;
    protected final Moneda moneda;
    protected final Client titular;

    public ContBancar(String iban, double sold, Moneda moneda, Client titular) {
        this.iban = iban;
        this.sold = sold;
        this.moneda = moneda;
        this.titular = titular;
    }

    public abstract String getTipCont();

    @Override
    public void depunere(double suma) {
        if (suma > 0) {
            this.sold += suma;
        }
    }

    public String getIban() { return iban; }
    public Moneda getMoneda() { return moneda; }
    public double getSold() { return sold; }
    public Client getTitular() { return titular; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContBancar that = (ContBancar) o;
        return Objects.equals(iban, that.iban);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iban);
    }

    @Override
    public String toString() {
        return String.format("%s | IBAN: %s | Sold: %.2f %s | Titular: %s",
                getTipCont(), iban, sold, moneda, titular.getNume());
    }
}
