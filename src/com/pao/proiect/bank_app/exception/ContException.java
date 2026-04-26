package com.pao.proiect.bank_app.exception;

public class ContException extends BancaException {
    public ContException(String message) {
        super(message, "ERR-BNK-05");
    }
}
