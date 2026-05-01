package com.pao.laboratory09.exercise3;

public class Main {
    public static void main(String[] args) {
        CoadaTranzactii banda = new CoadaTranzactii();

        ATMThread atm1 = new ATMThread(1, banda);
        ATMThread atm2 = new ATMThread(2, banda);
        ATMThread atm3 = new ATMThread(3, banda);
        ProcessorThread processor = new ProcessorThread(banda);
        Thread firProcessor = new Thread(processor);

        atm1.start();
        atm2.start();
        atm3.start();
        firProcessor.start();

        try {
            atm1.join();
            atm2.join();
            atm3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        processor.activ = false;

        synchronized (banda) {
            banda.notifyAll();
        }

        try {
            firProcessor.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Toate tranzactiile procesate. Total: 12");
    }
}
