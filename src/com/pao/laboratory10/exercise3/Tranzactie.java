package com.pao.laboratory10.exercise3;

import com.pao.laboratory10.exercise1.TipTranzactie;

public class Tranzactie extends com.pao.laboratory10.exercise1.Tranzactie {
    String cont;

    public Tranzactie(int id, double suma, String data, TipTranzactie tip, String cont) {
        super(id, suma, data, tip);
        this.cont = cont;
    }

    public String getCont() {
        return cont;
    }

    public void setCont(String cont) {
        this.cont = cont;
    }

}
