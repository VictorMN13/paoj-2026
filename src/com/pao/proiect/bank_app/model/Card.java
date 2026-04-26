package com.pao.proiect.bank_app.model;

import java.time.LocalDate;

public class Card {
    private final String numarCard;
    private final String ibanAsociat;
    private final String cvv;
    private final LocalDate dataExpirare;
    private String pin;
    private boolean blocat;

    public Card(String numarCard, String ibanAsociat, String cvv, String pin, boolean blocat) {
        this.numarCard = numarCard;
        this.ibanAsociat = ibanAsociat;
        this.cvv = cvv;
        this.dataExpirare = LocalDate.now().plusYears(4);
        this.pin = pin;
        this.blocat = blocat;
    }

    public String getNumarCard() {
        return numarCard;
    }

    public String getIbanAsociat() {
        return ibanAsociat;
    }

    public String getCvv() {
        return cvv;
    }

    public LocalDate getDataExpirare() {
        return dataExpirare;
    }

    public String getPin() {
        return pin;
    }

    public boolean isBlocat() {
        return blocat;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setBlocat(boolean blocat) {
        this.blocat = blocat;
    }

    @Override
    public String toString() {
        return "Card{" +
                "numarCard='" + numarCard + '\'' +
                ", ibanAsociat='" + ibanAsociat + '\'' +
                ", cvv='" + cvv + '\'' +
                ", dataExpirare=" + dataExpirare +
                ", pin='" + pin + '\'' +
                ", blocat=" + blocat +
                '}';
    }
}
