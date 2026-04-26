package com.pao.proiect.bank_app.exception;

import java.time.LocalDateTime;

public abstract class BancaException extends Exception {
    private final String codEroare;
    private final LocalDateTime timestamp;

    public BancaException(String mesaj, String codEroare) {
        super(mesaj);
        this.codEroare = codEroare;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String getMessage() {
        return String.format("[%s] COD: %s | MESAJ: %s", timestamp, codEroare, super.getMessage());
    }
}
