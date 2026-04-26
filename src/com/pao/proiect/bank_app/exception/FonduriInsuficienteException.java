package com.pao.proiect.bank_app.exception;

public class FonduriInsuficienteException extends BancaException {
    public FonduriInsuficienteException(String message) {
        super(message, "ERR-BNK-01");
    }
}
