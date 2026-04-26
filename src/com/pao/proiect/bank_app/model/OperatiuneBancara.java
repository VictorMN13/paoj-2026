package com.pao.proiect.bank_app.model;

import com.pao.proiect.bank_app.exception.BancaException;
import com.pao.proiect.bank_app.exception.FonduriInsuficienteException;

public interface OperatiuneBancara {
    void depunere(double suma);
    void retragere(double suma) throws FonduriInsuficienteException;

    default void afiseazaMesajSucces(String mesaj) {
        System.out.println("[INFO BANCA] " + mesaj);
    }
}
