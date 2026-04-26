package com.pao.proiect.bank_app.exception;

public class UserException extends BancaException {
    public UserException(String message) {
        super(message, "ERR-BNK-04");
    }
}
