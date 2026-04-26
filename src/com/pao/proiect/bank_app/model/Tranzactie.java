package com.pao.proiect.bank_app.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record Tranzactie(
        String idTranzactie,
        LocalDateTime timestamp,
        double suma,
        TipTranzactie tip,
        String ibanSursa,
        String ibanDestinatie
) implements Comparable<Tranzactie> {
    public Tranzactie {
        if (suma <= 0) {
            throw new IllegalArgumentException("Suma unei tranzactii trebuie sa fie strict pozitiva!");
        }

        if (ibanSursa != null && !ibanSursa.startsWith("RO")) {
            throw new IllegalArgumentException("IBAN sursa invalid (trebuie sa inceapa cu RO)");
        }

        if (ibanDestinatie != null && !ibanDestinatie.startsWith("RO")) {
            throw new IllegalArgumentException("IBAN destinatie invalid (trebuie sa inceapa cu RO)");
        }
    }

    public Tranzactie(double suma, TipTranzactie tip, String ibanSursa, String ibanDestinatie) {
        this(UUID.randomUUID().toString(), LocalDateTime.now(), suma, tip, ibanSursa, ibanDestinatie);
    }

    @Override
    public String toString() {
        String sursa = (ibanSursa != null) ? ibanSursa : "CASH";
        String destinatie = (ibanDestinatie != null) ? ibanDestinatie : "CASH";

        return String.format("[%s] %s | Suma: %.2f | De la: %s -> Catre: %s (ID: %s)",
                timestamp.toLocalDate() + " " + timestamp.toLocalTime().withNano(0),
                tip, suma, sursa, destinatie,
                idTranzactie.substring(0, 8));
    }

    @Override
    public int compareTo(Tranzactie o) {
        return o.timestamp.compareTo(timestamp);
    }
}
