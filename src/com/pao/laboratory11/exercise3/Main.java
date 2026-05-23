package com.pao.laboratory11.exercise3;

import com.pao.laboratory11.exercise3.CustomCollector;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // TODO: Manual demo for bonus requirements.
        List<Transaction> data = List.of(
                new Transaction(1, new BigDecimal("1500.00"), LocalDate.of(2026, 5, 1), "RO", "WEB"),
                new Transaction(2, new BigDecimal("5000.00"), LocalDate.of(2026, 5, 2), "RU", "CRYPTO"),
                new Transaction(3, new BigDecimal("5000.00"), LocalDate.of(2026, 5, 3), "NG", "CRYPTO"),
                new Transaction(4, new BigDecimal("250.00"),  LocalDate.of(2026, 5, 4), "RO", "APP"),
                new Transaction(5, new BigDecimal("250.00"),  LocalDate.of(2026, 5, 5), "IT", "POS"),
                new Transaction(6, new BigDecimal("1500.00"), LocalDate.of(2026, 5, 6), "RO", "WEB")
        );

        Snapshot snap = data.stream().collect(CustomCollector.toSnapshot(3));

        System.out.println("=== REZUMAT GENERAL ===");
        System.out.println("Suma totala procesata: " + snap.getTotalAmount() + " RON\n");

        System.out.println("=== TOP 3 TRANZACTII ===");
        snap.getTopTransactions().forEach(System.out::println);

        System.out.println("\n=== TRANZACTII PE TARI (Ordonate descrescator) ===");
        snap.getCountByCountry().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue() + " txs"));

        System.out.println("\n=== TRANZACTII PE CANALE (Ordonate descrescator) ===");
        snap.getCountByChannel().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue() + " txs"));
    }
}
