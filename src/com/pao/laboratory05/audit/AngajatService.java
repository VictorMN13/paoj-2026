package com.pao.laboratory05.audit;
import java.time.LocalDateTime;
import java.util.Arrays;

public class AngajatService {
    private Angajat[] angajati =  new Angajat[0];
    private AuditEntry[] auditLog = new AuditEntry[0];

    private AngajatService() {}

    private static class Holder {
        private static AngajatService instance = new AngajatService();
    }

    public static AngajatService getInstance() {
        return Holder.instance;
    }

    private void logAction(String action, String target) {
        AuditEntry a = new AuditEntry(action, target, LocalDateTime.now().toString());
        AuditEntry[] temp = new AuditEntry[auditLog.length + 1];
        System.arraycopy(auditLog, 0, temp, 0, auditLog.length);
        temp[auditLog.length] = a;
        auditLog = temp;
    }

    void addAngajat(Angajat angajat) {
        Angajat[] temp = new Angajat[angajati.length+1];
        System.arraycopy(angajati, 0, temp, 0, angajati.length);
        temp[angajati.length] = angajat;
        angajati = temp;
        logAction("ADD", angajat.getNume());
        System.out.println("Angajat " + angajat.getNume() + " adaugat cu succes");
    }

    void printAll() {
        for (Angajat angajat : angajati) {
            System.out.println(angajat);
        }
    }

    void listBySalary() {
        Angajat[] temp = new Angajat[angajati.length];
        System.arraycopy(angajati, 0, temp, 0, angajati.length);
        Arrays.sort(temp);
        for (Angajat angajat : temp) {
            System.out.println(angajat);
        }
    }

    void findByDepartament(String numeDept) {
        logAction("FIND_BY_DEPT", numeDept);
        boolean gasit = false;
        for (Angajat angajat : angajati) {
            if (angajat.getDepartament().nume().equalsIgnoreCase(numeDept)) {
                gasit = true;
                System.out.println(angajat);
            }
        }
        if (!gasit) {
            System.out.println("Nici un angajat in deoartamentul: " + numeDept);
        }
    }

    void printAuditLog() {
        for (AuditEntry a : auditLog) {
            System.out.println(a);
        }
    }
}
