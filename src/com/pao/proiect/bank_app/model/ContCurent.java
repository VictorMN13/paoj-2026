package com.pao.proiect.bank_app.model;

import com.pao.proiect.bank_app.exception.BancaException;
import com.pao.proiect.bank_app.exception.FonduriInsuficienteException;

import java.util.ArrayList;
import java.util.List;

public class ContCurent extends ContBancar{
    private double comisionAdministrare;
    private List<Card> carduriAtasate;

    public ContCurent(String iban, double sold, Moneda moneda, Client titular, double comisionAdministrare) {
        super(iban, sold, moneda, titular);
        this.comisionAdministrare = comisionAdministrare;
        this.carduriAtasate = new ArrayList<>();
    }

    @Override
    public String getTipCont() {
        return "Cont Curent";
    }

    @Override
    public void retragere(double suma) throws FonduriInsuficienteException {
        if (suma > sold) {
            throw new FonduriInsuficienteException("Fonduri insuficiente in contul curent! Sold Curent: " + sold);
        }
        this.sold -= suma;
    }

    public void adaugaCard(Card card) {
        this.carduriAtasate.add(card);
    }

    public List<Card> getCarduriAtasate() {
            return new ArrayList<>(carduriAtasate);
    }

    public void platesteComAdmin() throws FonduriInsuficienteException {
        if (comisionAdministrare > sold) {
            throw new FonduriInsuficienteException("Fonduri insuficiente pentru comisionul de administrare pe contul " + iban);
        }
        this.sold -= comisionAdministrare;
    }

    public double getComisionAdministrare() {
        return comisionAdministrare;
    }
    public void setComisionAdministrare(double comisionAdministrare) {
        this.comisionAdministrare = comisionAdministrare;
    }
}
