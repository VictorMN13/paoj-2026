package com.pao.laboratory07.exercise3;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        List<Comanda> comenzi = new ArrayList<>();
        int nrStandard = 0, nrDiscounted = 0, nrGift = 0;
        double sumaStandard = 0, sumaDiscounted = 0;
        for (int i = 0; i < n; i++) {
            String line = sc.nextLine().trim();
            String[] tokens = line.split(" ");
            if (tokens[0].equals("STANDARD")) {
                String nume = tokens[1];
                double pret = Double.parseDouble(tokens[2]);
                String client =  tokens[3];
                Comanda c = new ComandaStandard(nume, pret, client);
                comenzi.add(c);
                nrStandard++;
                sumaStandard += c.pretFinal();
            } else if (tokens[0].equals("DISCOUNTED")) {
                String nume = tokens[1];
                double pret = Double.parseDouble(tokens[2]);
                int discount = Integer.parseInt(tokens[3]);
                String client =  tokens[4];
                Comanda c = new ComandaRedusa(nume, pret, discount, client);
                comenzi.add(c);
                nrDiscounted++;
                sumaDiscounted += c.pretFinal();
            } else if (tokens[0].equals("GIFT")) {
                String nume = tokens[1];
                String client =  tokens[2];
                Comanda c = new ComandaGratuita(nume, client);
                comenzi.add(c);
                nrGift++;
            }
        }
        System.out.println();
        for (Comanda c : comenzi) {
            System.out.println(c.descriere());
        }
        String command = sc.next();
        while (!command.equals("QUIT")) {
            switch (command) {
                case "STATS" -> {
                    System.out.println("\n--- STATS ---");
                    Map<String, Double> medii = comenzi.stream()
                            .collect(Collectors.groupingBy(
                                    Comanda::getTip,
                                    Collectors.averagingDouble(Comanda::pretFinal)
                            ));

                    List.of("STANDARD", "DISCOUNTED", "GIFT").forEach(tip -> {
                        if (medii.containsKey(tip)) {
                            System.out.printf("%s: medie = %.2f lei\n", tip, medii.get(tip));
                        }
                    });
                }
                case "FILTER" -> {
                    System.out.println("\n--- FILTER ---");
                    double threshold = sc.nextDouble();
                    comenzi.stream()
                            .filter(c -> c.pretFinal() >= threshold)
                            .forEach(c -> System.out.printf("%s: %s, pret: %.2f lei - client: %s\n",
                                    c.getTip(), c.nume, c.pretFinal(), c.client));
                }
                case "SORT" -> {
                    System.out.println("\n--- SORT ---");
                    comenzi.stream()
                            .sorted(Comparator.comparing((Comanda c) -> switch (c.getTip()) {
                                        case "STANDARD" -> 1;
                                        case "DISCOUNTED" -> 2;
                                        case "GIFT" -> 3;
                                        default -> 4;
                                    }).thenComparing(c -> c.client)
                                    .thenComparing(Comanda::pretFinal))
                            .forEach(c -> System.out.printf("%s: %s, pret: %.2f lei - client: %s\n",
                                    c.getTip(), c.nume, c.pretFinal(), c.client));
                }
                case "SPECIAL" -> {
                    System.out.println("\n--- SPECIAL ---");
                    comenzi.stream()
                            .filter(c -> c instanceof ComandaRedusa cr && cr.discount > 15)
                            .forEach(c -> System.out.println(c.descriere()));
                }
            }
            command = sc.next();
        }
    }
}
