package com.pao.proiect.bank_app.model;

import com.pao.proiect.bank_app.exception.FonduriInsuficienteException;

public class ContEconomii extends ContBancar{
    private double rataDobanda;

    public ContEconomii(String iban, double sold, Moneda moneda, Client titular, double rataDobanda) {
        super(iban, sold, moneda, titular);
        this.rataDobanda = rataDobanda;
    }

    @Override
    public String getTipCont() {
        return "Cont de Economii";
    }

    @Override
    public void retragere(double suma) throws FonduriInsuficienteException {
        if (suma > sold) {
            throw new FonduriInsuficienteException("Fonduri insuficiente pe contul de economii! Sold Curent: " + sold);
        }
        this.sold -= suma;
    }

    public void aplicaDobanda() {
        double profit = this.sold * (rataDobanda / 100);
        this.sold += profit;
    }

    public double getRataDobanda() {
        return rataDobanda;
    }

    public void setRataDobanda(double rataDobanda) {
        this.rataDobanda = rataDobanda;
    }
}
