package com.pao.laboratory09.exercise1;

import java.io.Serial;
import java.io.Serializable;

public class Tranzactie implements Serializable {
    int id;
    double suma;
    String data;
    String contSursa;
    String contDestintatie;
    TipTranzactie tip;
    transient String note;
    @Serial
    private static final long serialVersionUID = 1L;

    public Tranzactie(int id, double suma, String data, String contSursa, String contDestintatie, TipTranzactie tip, String note) {
        this.id = id;
        this.suma = suma;
        this.data = data;
        this.contSursa = contSursa;
        this.contDestintatie = contDestintatie;
        this.tip = tip;
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s %s: %.2f RON | %s -> %s", id, data, tip, suma, contSursa, contDestintatie);
    }
}
