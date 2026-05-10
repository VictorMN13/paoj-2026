package com.pao.laboratory10.exercise1;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        LinkedList<Tranzactie> tranzactii = new LinkedList();

        Scanner sc = new Scanner(System.in);
        String cmd = "";
        while(sc.hasNextLine()) {
            cmd = sc.nextLine();
            String[] tokens = cmd.split(" ");
            switch (tokens[0]) {
                case "ENQUEUE": {
                    Tranzactie t = new Tranzactie(Integer.parseInt(tokens[1]), Double.parseDouble(tokens[2]), tokens[3], TipTranzactie.valueOf(tokens[4]));
                    tranzactii.addLast(t);
                    break;
                }
                case "DEQUEUE": {
                    if (tranzactii.isEmpty()) {
                        System.out.println("Coada goala.");
                        break;
                    }
                    Tranzactie t = (Tranzactie) tranzactii.removeFirst();
                    System.out.println("Procesat: " + t);
                    break;
                }
                case "PUSH": {
                    Tranzactie t = new Tranzactie(Integer.parseInt(tokens[1]), Double.parseDouble(tokens[2]), tokens[3], TipTranzactie.valueOf(tokens[4]));
                    tranzactii.addFirst(t);
                    break;
                }
                case "POP": {
                    if (tranzactii.isEmpty()) {
                        System.out.println("Coada goala.");
                        break;
                    }
                    Tranzactie t = (Tranzactie) tranzactii.removeFirst();
                    System.out.println("Extras: " + t);
                    break;
                }
                case "REMOVE_DEBIT": {
                    Iterator<Tranzactie> it = tranzactii.iterator();
                    int cnt = 0;
                    while (it.hasNext()) {
                        Tranzactie t = (Tranzactie) it.next();
                        if (t.tip == TipTranzactie.DEBIT) {
                            it.remove();
                            cnt++;
                        }
                    }
                    System.out.println("Eliminat " + cnt +" tranzactii DEBIT.");
                    break;
                }
                case "REMOVE_BELOW": {
                    double treshhold = Double.parseDouble(tokens[1]);
                    Iterator<Tranzactie> it = tranzactii.iterator();
                    int cnt = 0;
                    while (it.hasNext()) {
                        Tranzactie t = (Tranzactie) it.next();
                        if (t.suma < treshhold) {
                            cnt ++;
                            it.remove();
                        }
                    }
                    System.out.printf("Eliminat %d tranzactii sub %.2f RON.%n", cnt, treshhold);
                    break;
                }
                case "PRINT": {
                    Iterator<Tranzactie> it = tranzactii.iterator();
                    while (it.hasNext()) {
                        Tranzactie t = (Tranzactie) it.next();
                        System.out.println(t);
                    }
                    break;
                }
                case "SIZE": {
                    System.out.println("Dimensiune coada: " +  tranzactii.size());
                    break;
                }
            }
        }

        // TODO: Implementează conform Readme.md
        //
        // Folosește LinkedList<Tranzactie> ca structură internă.
        // Citește comenzi din stdin până la EOF:
        //
        //   ENQUEUE id suma data tip   → addLast  (niciun output)
        //   DEQUEUE                    → removeFirst sau "Coada goala."
        //                                format: "Procesat: [id] data tip: suma RON"
        //   PUSH id suma data tip      → addFirst  (niciun output)
        //   POP                        → removeFirst sau "Coada goala."
        //                                format: "Extras: [id] data tip: suma RON"
        //   REMOVE_DEBIT               → Iterator.remove() pe toate DEBIT
        //                                afișează "Eliminat N tranzactii DEBIT."
        //   REMOVE_BELOW threshold     → Iterator.remove() pe suma < threshold
        //                                afișează "Eliminat N tranzactii sub threshold RON."
        //   PRINT                      → afișează toate, câte una pe linie
        //   SIZE                       → "Dimensiune coada: N"
        //
        // Format linie tranzacție: [id] data tip: suma RON
        //   Ex: [1] 2024-01-10 CREDIT: 500.00 RON
    }
}
