package com.pao.laboratory08.exercise2;

import java.io.*;
import java.util.*;
import com.pao.laboratory08.exercise1.Adresa;
import com.pao.laboratory08.exercise1.Student;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        int prag = sc.nextInt();
        List<Student> stud = citire();
        List<Student> filtrat = new ArrayList<>();
        if (stud.isEmpty()) {
            System.out.println("No student found");
        }
        else {
            System.out.println("Filtru: varsta >= " + prag);
            for (Student s : stud) {
                if (s.getVarsta() >= prag) {
                    filtrat.add(s);
                }
            }
            BufferedWriter fout = new BufferedWriter(new FileWriter("rezultate.txt"));
            System.out.println("Rezultate: " +  filtrat.size() + " studenti\n");
            for (Student s : filtrat) {
                System.out.println(s);
                fout.write(s.toString());
                fout.write("\n");
            }
            System.out.println();
            fout.close();
            System.out.println("Scris in: rezultate.txt");
        }

        // TODO: Implementează conform Readme.md
        //
        // 1. Citește studenții din FILE_PATH cu BufferedReader
        // 2. Citește pragul de vârstă din stdin cu Scanner
        // 3. Filtrează studenții cu varsta >= prag
        // 4. Scrie filtrații în "rezultate.txt" cu BufferedWriter
        // 5. Afișează sumarul la consolă

//        System.out.println("TODO: implementează exercițiul 2");
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

