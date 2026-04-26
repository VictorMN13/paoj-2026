package com.pao.proiect.bank_app.exception;

public class TranzactieInvalidaException extends BancaException {
    public TranzactieInvalidaException(String message) {
        super(message, "ERR-BNK-03");
    }
}
