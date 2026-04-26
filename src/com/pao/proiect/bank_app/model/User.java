package com.pao.proiect.bank_app.model;

import java.util.UUID;

public abstract class User {
    protected final String id;
    protected String nume;
    protected String prenume;
    protected String email;
    protected String parola;

    public User(String nume, String prenume, String email, String parola) {
        this.id = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
        this.parola = parola;
    }

    public abstract String getRol();

    public String getId() {
        return id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (ID: %s, Email: %s)", getRol(), (getNume() + " " + getPrenume()), id, email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return java.util.Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(email);
    }
}
