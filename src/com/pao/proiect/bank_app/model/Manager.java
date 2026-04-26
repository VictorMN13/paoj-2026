package com.pao.proiect.bank_app.model;

public class Manager extends Angajat{
    private double bonusConducere;
    private String departament;

    public Manager(String nume, String prenume, String email, String parola, double salariu, String sucursala, double bonusConducere, String departament) {
        super(nume, prenume, email, parola, salariu, sucursala);
        this.bonusConducere = bonusConducere;
        this.departament = departament;
    }

    @Override
    public String getRol() {
        return "Manager";
    }

    public double getBonusConducere() {
        return bonusConducere;
    }

    public void setBonusConducere(double bonusConducere) {
        this.bonusConducere = bonusConducere;
    }

    public String getDepartament() {
        return departament;
    }

    public void setDepartament(String departament) {
        this.departament = departament;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Departament: %s | Bonus: %.2f", departament, bonusConducere);
    }
}
