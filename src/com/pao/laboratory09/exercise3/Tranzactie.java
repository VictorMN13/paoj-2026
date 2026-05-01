package com.pao.laboratory09.exercise3;

public class Tranzactie {
    private static int contorId = 0;
    public int id;
    public double suma;
    public String data;

    public Tranzactie(double suma, String data) {
        synchronized (Tranzactie.class) {
            this.id = ++contorId;
        }
        this.suma = suma;
        this.data = data;
    }
}
