package com.pao.laboratory07.exercise3;
import com.pao.laboratory07.exercise1.OrderState;

import java.util.Stack;

public abstract sealed class Comanda permits ComandaStandard, ComandaRedusa, ComandaGratuita {
    protected String nume;
    protected double pret;
    protected String client;
    private OrderState currentState;
    private final Stack<OrderState> history = new Stack<>();

    public Comanda(String nume, double pret,  String client) {
        this.nume = nume;
        this.pret = pret;
        this.client = client;
    }

    public abstract String getTip();
    public abstract double pretFinal();
    public abstract String descriere();
}