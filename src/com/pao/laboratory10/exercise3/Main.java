package com.pao.laboratory10.exercise3;

import com.pao.laboratory10.exercise1.TipTranzactie;
import java.util.*;
import java.util.stream.Collectors;

import static com.pao.laboratory10.exercise1.TipTranzactie.CREDIT;
import static java.util.stream.Collectors.toList;

public class Main {
    public static void main(String[] args) {
        List<Tranzactie> tranzactiiTest = new ArrayList<>(Arrays.asList(
                new Tranzactie(1, 4500.00, "2026-01-05", CREDIT, "CONT_SALARIU"),
                new Tranzactie(2, 250.50, "2026-01-10", TipTranzactie.DEBIT, "CONT_CURENT"),
                new Tranzactie(3, 150.00, "2026-01-15", TipTranzactie.DEBIT, "CONT_CURENT"),
                new Tranzactie(4, 500.00, "2026-01-20", CREDIT, "CONT_ECONOMII"),

                new Tranzactie(5, 4500.00, "2026-02-05", CREDIT, "CONT_SALARIU"),
                new Tranzactie(6, 800.00, "2026-02-12", TipTranzactie.DEBIT, "CONT_COMUN"),
                new Tranzactie(7, 45.99, "2026-02-18", TipTranzactie.DEBIT, "CONT_CURENT"),

                new Tranzactie(8, 4500.00, "2026-03-05", CREDIT, "CONT_SALARIU"),
                new Tranzactie(9, 1200.00, "2026-03-10", TipTranzactie.DEBIT, "CONT_COMUN"),
                new Tranzactie(10, 320.75, "2026-03-25", TipTranzactie.DEBIT, "CONT_CURENT")
        ));
        // op 1
        System.out.println("=== Tranzactiile de tip Credit ===");
        tranzactiiTest.stream().
                filter(t -> t.getTip() == CREDIT).
                forEach(System.out::println);
        // op 2
        System.out.println("\n=== Suma tuturor tranzactiilor ===");
        double sum = tranzactiiTest.stream().mapToDouble(Tranzactie::getSuma).sum();
        System.out.printf("Total procesat: %.2f RON", sum);
        // op 3
        System.out.println("\n=== Totaluri tranzactii lunare ===");
        tranzactiiTest.stream().collect(Collectors.groupingBy(t -> t.getData().substring(0, 7),
                Collectors.summingDouble(Tranzactie::getSuma))).
                forEach((luna, suma) -> System.out.printf("%s: %.2f RON\n", luna, suma));
        // op 4
        System.out.println("\n=== Primele 3 tranzactii ===");
        tranzactiiTest.stream().sorted(Comparator.comparingDouble(Tranzactie::getSuma).reversed()).limit(3).
                forEach(System.out::println);
        // op 5
        System.out.println("\n=== Conturi unice ===");
        tranzactiiTest.stream().map(Tranzactie::getCont).distinct().toList().forEach(System.out::println);
        // op 6
        System.out.println("\n=== Suma medie ===");
        tranzactiiTest.stream().mapToDouble(Tranzactie::getSuma).average().ifPresent(System.out::println);
        // op 7
        System.out.println("\n=== Extrase de cont lunare ===");
        tranzactiiTest.stream().collect(Collectors.groupingBy(t -> t.getData().substring(0, 7),
                    Collectors.summarizingDouble(Tranzactie::getSuma))).
                    forEach((luna, suma) -> System.out.printf("%s: %d tranzactii, total: %.2f RON\n", luna, suma.getCount(), suma.getSum()));
    }
}
