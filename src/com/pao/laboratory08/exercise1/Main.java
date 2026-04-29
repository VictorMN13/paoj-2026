package com.pao.laboratory08.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    // Calea către fișierul cu date — relativă la rădăcina proiectului
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String command = sc.next();
            switch (command) {
                case "PRINT" -> {
                    List<Student> stud = citire();
                    if (stud.isEmpty()) {
                        System.out.println("No student found");
                    }
                    else {
                        for (Student s : stud) {
                            System.out.println(s);
                        }
                    }
                }
                case "SHALLOW" -> {
                    String name = sc.next();
                    List<Student> stud = citire();
                    if (stud.isEmpty()) {
                        System.out.println("No student found");
                    }
                    else {
                        for (Student s : stud) {
                            if (s.getNume().equals(name)) {
                                Student clona = s.clone(false);
                                clona.getAdresa().setOras("MODIFICAT");
                                System.out.println("Original: " + s);
                                System.out.println("Clona: " + clona);
                            }
                        }
                    }

                }
                case "DEEP" -> {
                    String name = sc.next();
                    List<Student> stud = citire();
                    if (stud.isEmpty()) {
                        System.out.println("No student found");
                    }
                    else {
                        for (Student s : stud) {
                            if (s.getNume().equals(name)) {
                                Student clona = s.clone(true);
                                clona.getAdresa().setOras("MODIFICAT");
                                System.out.println("Original: " + s);
                                System.out.println("Clona: " + clona);
                            }
                        }
                    }

                }
            }
        }

        // TODO: Implementează conform Readme.md
        //
        // 1. Citește studenții din FILE_PATH cu BufferedReader
        // 2. Citește comanda din stdin: PRINT, SHALLOW <nume> sau DEEP <nume>
        // 3. Execută comanda:
        //    - PRINT → afișează toți studenții
        //    - SHALLOW <nume> → shallow clone + modifică orașul clonei la "MODIFICAT" + afișează
        //    - DEEP <nume> → deep clone + modifică orașul clonei la "MODIFICAT" + afișează

//        System.out.println("TODO: implementează exercițiul 1");
    }
    public static List<Student> citire() throws Exception {
        List<Student> stud = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(FILE_PATH));
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] tokens = line.split(",");
            String name = tokens[0];
            int varsta = Integer.parseInt(tokens[1]);
            String oras = tokens[2];
            String strada = tokens[3];

            Adresa adr = new Adresa(oras, strada);
            Student std = new Student(name, varsta, adr);
            stud.add(std);
        }
        br.close();
        return stud;
    }
}
