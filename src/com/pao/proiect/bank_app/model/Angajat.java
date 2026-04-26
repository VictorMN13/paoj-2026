package com.pao.proiect.bank_app.model;

public class Angajat extends  User{
    protected double salariu;
    protected String sucursala;

    public Angajat(String nume, String prenume, String email, String parola, double salariu, String sucursala) {
        super(nume, prenume, email, parola);
        this.salariu = salariu;
        this.sucursala = sucursala;
    }

    @Override
    public String getRol() {
        return "Angajat";
    }

    public double getSalariu() {
        return salariu;
    }

    public void setSalariu(double salariu) {
        this.salariu = salariu;
    }

    public String getSucursala() {
        return sucursala;
    }

    public void setSucursala(String sucursala) {
        this.sucursala = sucursala;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Sucursala: %s", sucursala);
    }
}
