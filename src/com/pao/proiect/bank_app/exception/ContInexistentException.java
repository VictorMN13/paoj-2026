package com.pao.proiect.bank_app.exception;

public class ContInexistentException extends BancaException {
    public ContInexistentException(String message) {
        super(message, "ERR-BNK-02");
    }

    public ContInexistentException(String identificator, boolean esteCont) {
        super(String.format("%s cu identificatorul '%s' nu a fost gasit in sistemul bancii!",
                esteCont ? "Contul" : "Utilizatorul", identificator), "ERR-BNK-02");
    }
}
