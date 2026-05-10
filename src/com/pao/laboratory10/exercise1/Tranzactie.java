package com.pao.laboratory10.exercise1;

public class Tranzactie {
    int id;
    double suma;
    String data;
    TipTranzactie tip;

    public Tranzactie(int id, double suma, String data, TipTranzactie tip) {
        this.id = id;
        this.suma = suma;
        this.data = data;
        this.tip = tip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSuma() {
        return suma;
    }

    public void setSuma(double suma) {
        this.suma = suma;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public TipTranzactie getTip() {
        return tip;
    }

    public void setTip(TipTranzactie tip) {
        this.tip = tip;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s %s: %.2f RON",  id, data, tip, suma);
    }
}
