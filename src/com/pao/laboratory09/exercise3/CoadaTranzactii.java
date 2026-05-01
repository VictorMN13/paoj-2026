package com.pao.laboratory09.exercise3;

import java.util.LinkedList;
import java.util.Queue;

public class CoadaTranzactii {
    private final int CAPACITATE = 5;
    private final Queue<Tranzactie> banda = new LinkedList<>();

    public synchronized void adauga(Tranzactie t, int atmId) {
        while (banda.size() == CAPACITATE) {
            System.out.println("[ATM-" + atmId + "] astept loc...");
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        banda.add(t);
        System.out.printf("[ATM-%d] trimite: Tranzactie #%d %.2f RON\n", atmId, t.id, t.suma);
        notifyAll();
    }

    public synchronized Tranzactie extrage() {
        while (banda.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            if (banda.isEmpty()) {
                return null;
            }
        }

        Tranzactie t = banda.poll();
        notifyAll();
        return t;
    }

    public synchronized boolean isEmpty() {
        return banda.isEmpty();
    }
}
