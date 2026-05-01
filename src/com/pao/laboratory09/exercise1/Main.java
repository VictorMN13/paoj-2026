package com.pao.laboratory09.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        sc.nextLine();
        List<Tranzactie> tranzactii = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String tranzactie = sc.nextLine();
            String[] tokens = tranzactie.split(" ");
            int id =  Integer.parseInt(tokens[0]);
            double suma = Double.parseDouble(tokens[1]);
            String data = tokens[2];
            String contSursa = tokens[3];
            String contDestinatie = tokens[4];
            TipTranzactie tip = TipTranzactie.valueOf(tokens[5]);
            tranzactii.add(new Tranzactie(id, suma, data, contSursa, contDestinatie, tip, "procesat"));
        }
        String filePath = "output/lab09_ex1.ser";
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(tranzactii);
        } catch (IOException e) {
            System.err.println("Eroare la serializare: " + e.getMessage());
        }

        tranzactii = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            tranzactii = (List<Tranzactie>) in.readObject();
        } catch (IOException e) {
            System.err.println("Eroare la deserializare: " + e.getMessage());
        }

        while (sc.hasNext()) {
            String command = sc.next();

            switch (command) {
                case "LIST" -> {
                    for (Tranzactie t : tranzactii) {
                        System.out.println(t);
                    }
                }

                case "FILTER" -> {
                    String lunaAn = sc.next();
                    boolean gasit = false;

                    for (Tranzactie t : tranzactii) {
                        if (t.data.startsWith(lunaAn)) {
                            System.out.println(t);
                            gasit = true;
                        }
                    }

                    if (!gasit) {
                        System.out.println("Niciun rezultat.");
                    }
                }

                case "NOTE" -> {
                    int idCautat = sc.nextInt();
                    boolean gasit = false;

                    for (Tranzactie t : tranzactii) {
                        if (t.id == idCautat) {
                            System.out.println("NOTE[" + idCautat + "]: " + t.getNote());
                            gasit = true;
                            break;
                        }
                    }

                    if (!gasit) {
                        System.out.println("NOTE[" + idCautat + "]: not found");
                    }
                }

                default -> {
                    sc.nextLine();
                    System.out.println("Comandă necunoscută.");
                }
            }
        }

        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data contSursa contDestinatie tip)
        // 2. Setează câmpul note = "procesat" pe fiecare tranzacție înainte de serializare
        // 3. Serializează lista de tranzacții în OUTPUT_FILE cu ObjectOutputStream (try-with-resources)
        // 4. Deserializează lista din OUTPUT_FILE cu ObjectInputStream (try-with-resources)
        // 5. Procesează comenzile din stdin până la EOF:
        //    - LIST          → afișează toate tranzacțiile, câte una pe linie
        //    - FILTER yyyy-MM → afișează tranzacțiile cu data care începe cu yyyy-MM
        //                       sau "Niciun rezultat." dacă nu există
        //    - NOTE id        → afișează "NOTE[id]: <valoarea câmpului note>"
        //                       sau "NOTE[id]: not found" dacă id-ul nu există
        //
        // Format linie tranzacție:
        //   [id] data tip: suma RON | contSursa -> contDestinatie
        //   Ex: [1] 2024-01-15 CREDIT: 1500.00 RON | RO01SRC1 -> RO01DST1
    }
}
