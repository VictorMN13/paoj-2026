package com.pao.laboratory09.exercise3;

public class ATMThread extends Thread{
    private final int atmId;
    private final CoadaTranzactii banda;

    public ATMThread(int atmId, CoadaTranzactii banda) {
        this.atmId = atmId;
        this.banda = banda;
    }

    @Override
    public void run() {
        for (int i = 0; i < 4; i++) {
            double suma = 100 + Math.random() * 900;
            Tranzactie t = new Tranzactie(suma, "2026-05-01");

            banda.adauga(t, atmId);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
