package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.Tranzactie;
import com.pao.laboratory10.exercise1.TipTranzactie;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        sc.nextLine();
        List<Tranzactie> tranzactii = new ArrayList<>();
        for  (int i = 0; i < n; i++) {
            String line =  sc.nextLine();
            String[] tokens = line.split(" ");
            Tranzactie t = new Tranzactie(Integer.parseInt(tokens[0]), Double.parseDouble(tokens[1]), tokens[2], TipTranzactie.valueOf(tokens[3]));
            tranzactii.add(t);
        }
        String cmd = "";
        while(sc.hasNextLine()) {
            cmd = sc.nextLine();
            String[] tokens = cmd.split(" ");
            switch (tokens[0]) {
                case "UNIQUE_IDS": {
                    LinkedHashSet<Integer> uniqueIds = new LinkedHashSet<>();
                    for(int i = 0; i < tranzactii.size(); i++) {
                        Tranzactie t = tranzactii.get(i);
                        uniqueIds.add(t.getId());
                    }
                    System.out.printf("IDs unice (%d): %s", uniqueIds.size(),  Arrays.toString(uniqueIds.toArray()));
                    break;
                }
                case "MONTHLY_REPORT": {
                    TreeMap<String, Double[]>  monthlyReport = new TreeMap<>();
                    for(int i = 0; i < tranzactii.size(); i++) {
                        Tranzactie t = tranzactii.get(i);
                        String data = t.getData().substring(0,7);
                        Double[] l = monthlyReport.get(data);
                        if (l == null) {
                            l = new Double[]{0.0, 0.0}; // credit debit
                        }
                        switch(t.getTip()) {
                            case DEBIT: {
                                l[1] += t.getSuma();
                                break;
                            }
                            case CREDIT: {
                                l[0] += t.getSuma();
                                break;
                            }
                        }
                        monthlyReport.put(data, l);
                    }
                    for (Map.Entry<String, Double[]> entry : monthlyReport.entrySet()) {
                        System.out.printf("%s: CREDIT %.2f RON, DEBIT %.2f RON\n", entry.getKey(), entry.getValue()[0], entry.getValue()[1]);
                    }
                    break;
                }
                case "TOP": {
                    int cnt = Integer.parseInt(tokens[1]);
                    List<Tranzactie> top = new ArrayList<>(tranzactii);
                    Collections.sort(top, Comparator.comparingDouble(Tranzactie::getSuma).reversed());
                    top = top.subList(0, cnt);
                    System.out.println("Top " + cnt);
                    for (Tranzactie t : top) {
                        System.out.println(t);
                    }
                    break;
                }
                case "SORT_ASC": {
                    Collections.sort(tranzactii, Comparator.comparingDouble(Tranzactie::getSuma));
                    for (Tranzactie t : tranzactii) {
                        System.out.println(t);
                    }
                    break;
                }
                case "SORT_DESC": {
                    Collections.sort(tranzactii, Comparator.comparingDouble(Tranzactie::getSuma).reversed());
                    for (Tranzactie t : tranzactii) {
                        System.out.println(t);
                    }
                    break;
                }
                case "REVERSE": {
                    Collections.reverse(tranzactii);
                    for (Tranzactie t : tranzactii) {
                        System.out.println(t);
                    }
                    break;
                }
                case "MIN_MAX": {
                    Tranzactie min = Collections.min(tranzactii, Comparator.comparingDouble(Tranzactie::getSuma));
                    Tranzactie max = Collections.max(tranzactii, Comparator.comparingDouble(Tranzactie::getSuma));
                    System.out.println("MIN: " + min);
                    System.out.println("MAX: " + max);
                    break;
                }
                case "CME_DEMO": {
                    try {
                        for (Tranzactie t : tranzactii) {
                            tranzactii.remove(t);
                        }
                    }
                    catch (ConcurrentModificationException e){
                        System.out.println("ConcurrentModificationException prins: modificare in iteratie detectata.");
                    }
                    break;
                }
            }
        }

        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data tip) — pot exista duplicate de id
        //    Stochează-le toate într-un ArrayList<Tranzactie> (cu duplicate, ordine inserare)
        //
        // 2. Procesează comenzile din stdin până la EOF:
        //
        //   UNIQUE_IDS      → LinkedHashSet<Integer> cu id-urile în ordinea primei apariții
        //                     afișează: "IDs unice (N): [1, 2, 3, ...]"
        //
        //   MONTHLY_REPORT  → TreeMap<String, ...> grupat pe yyyy-MM (substring 0-7 din data)
        //                     pentru fiecare lună, sumele CREDIT și DEBIT
        //                     format: "yyyy-MM: CREDIT X.XX RON, DEBIT Y.YY RON"
        //
        //   TOP n           → primele n tranzacții după suma descrescătoare (nu modifică lista)
        //                     afișează "Top n:" urmat de n linii
        //
        //   SORT_ASC        → Collections.sort cu suma crescătoare; afișează lista sortată
        //   SORT_DESC       → Collections.sort cu suma descrescătoare; afișează lista sortată
        //   REVERSE         → Collections.reverse; afișează lista
        //   MIN_MAX         → Collections.min/max după suma
        //                     "MIN: [id] data tip: suma RON"
        //                     "MAX: [id] data tip: suma RON"
        //
        //   CME_DEMO        → încearcă for(t : lista) lista.remove(t) în try-catch
        //                     afișează "ConcurrentModificationException prins: modificare in iteratie detectata."
        //
        // Format linie tranzacție: [id] data tip: suma RON
        //   Ex: [1] 2024-01-15 CREDIT: 1500.00 RON
    }
}
