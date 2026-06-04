package com.pao.proiect.bank_app.model;

public class Client extends User{
    private String adresa;
    private String numarTelefon;

    public Client(String nume, String prenume, String email, String parola, String adresa, String numarTelefon) {
        super(nume, prenume, email, parola);
        this.adresa = adresa;
        this.numarTelefon = numarTelefon;
    }

    public Client(String id, String nume, String prenume, String email, String parola, String adresa, String numarTelefon) {
        super(id, nume, prenume, email, parola);
        this.adresa = adresa;
        this.numarTelefon = numarTelefon;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getNumarTelefon() {
        return numarTelefon;
    }

    public void setNumarTelefon(String numarTelefon) {
        this.numarTelefon = numarTelefon;
    }

    @Override
    public String getRol() {
        return "Client";
    }
}
