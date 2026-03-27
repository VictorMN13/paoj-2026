package com.pao.laboratory05.angajati;

import java.util.Scanner;

/**
 * Exercise 3 — Angajați
 *
 * Cerințele complete se află în:
 *   src/com/pao/laboratory05/Readme.md  →  secțiunea "Exercise 3 — Angajați"
 *
 * Creează fișierele de la zero în acest pachet, apoi rulează Main.java
 * pentru a verifica output-ul așteptat din Readme.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Cerințele se află în Readme.md — secțiunea Exercise 3.");
        AngajatService aService = AngajatService.getInstance();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Gestionare Angajați =====");
            System.out.println("1. Adaugă angajat");
            System.out.println("2. Listare după salariu");
            System.out.println("3. Caută după departament");
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
