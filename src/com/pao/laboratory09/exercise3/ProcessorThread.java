package com.pao.laboratory09.exercise3;

public class ProcessorThread implements Runnable{
    public volatile boolean activ = true;
    private final CoadaTranzactii banda;

    public ProcessorThread(CoadaTranzactii banda) {
        this.banda = banda;
    }

    @Override
    public void run() {
        while (activ || !banda.isEmpty()) {
            Tranzactie t = banda.extrage();

            if (t != null) {
                System.out.printf("[Processor] Factura #%d - %.2f RON | %s\n", t.id, t.suma, t.data);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
