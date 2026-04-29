package com.pao.laboratory07.exercise2;
import com.pao.laboratory07.exercise1.OrderState;
import com.pao.laboratory07.exercise2.ComandaStandard;

import java.util.Stack;

public abstract sealed class Comanda permits ComandaStandard, ComandaRedusa, ComandaGratuita {
    protected String nume;
    protected double pret;
    private OrderState currentState;
    private final Stack<OrderState> history = new Stack<>();

    public Comanda(String nume, double pret) {
        this.nume = nume;
        this.pret = pret;
    }

    public abstract double pretFinal();
    public abstract String descriere();
}