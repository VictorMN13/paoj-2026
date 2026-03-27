package com.pao.laboratory05.audit;

import java.util.Scanner;

/**
 * Exercise 4 (Bonus) — Audit Log
 *
 * Cerințele complete se află în:
 *   src/com/pao/laboratory05/Readme.md  →  secțiunea "Exercise 4 (Bonus) — Audit"
 *
 * Extinde soluția de la Exercise 3 cu un sistem de audit bazat pe record.
 * Creează fișierele de la zero în acest pachet, apoi rulează Main.java
 * pentru a verifica output-ul așteptat din Readme.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Cerințele se află în Readme.md — secțiunea Exercise 4 (Bonus).");
        AngajatService aService = AngajatService.getInstance();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Gestionare Angajați =====");
            System.out.println("1. Adaugă angajat");
            System.out.println("2. Listare după salariu");
            System.out.println("3. Caută după departament");
            System.out.println("4. Afișează audit log");
            System.out.println("0. Ieșire");
            System.out.print("Opțiune: ");
            // citește opțiunea și execută acțiunea
            int opt = sc.nextInt();
            sc.nextLine();
            switch (opt) {
                case 1:
                    System.out.print("Nume: ");
                    String nume = sc.next();
                    sc.nextLine();

                    System.out.print("Departament (nume): ");
                    String numeDept = sc.next();
                    sc.nextLine();

                    System.out.print("Departament (locatie): ");
                    String locatie = sc.next();
                    sc.nextLine();

                    System.out.print("Salariu: ");
                    double salariu = sc.nextDouble();
                    sc.nextLine();

                    Departament d = new Departament(numeDept, locatie);
                    Angajat a = new Angajat(nume, d, salariu);
                    aService.addAngajat(a);
                    break;
                case 2:
                    aService.listBySalary();
                    break;
                case 3:
                    System.out.print("Departament (nume): ");
                    String numeDept1 = sc.next();
                    aService.findByDepartament(numeDept1);
                    break;
                case 4:
                    aService.printAuditLog();
                    break;
                case 0:
                    System.out.println("La revedere");
                    sc.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Optiune invalida");
                    break;
            }
        }
    }
}
