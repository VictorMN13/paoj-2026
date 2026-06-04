package com.pao.proiect.bank_app.service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.ReentrantLock;

public final class AuditService {

    private static AuditService instance;

    private static final String AUDIT_FILE = "audit.csv";

    private final ReentrantLock lock = new ReentrantLock();

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private AuditService() {
        java.io.File file = new java.io.File(AUDIT_FILE);
        if (!file.exists()) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(AUDIT_FILE, true))) {
                pw.println("nume_actiune,timestamp");
            } catch (IOException e) {
                System.err.println("Eroare la scrierea header-ului de audit: " + e.getMessage());
            }
        }
    }

    public static AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    /**
     * Logheaza o actiune in audit.csv.
     * Metoda este thread-safe prin ReentrantLock.
     *
     * @param actionName Numele actiunii (ex: "add_book", "borrow_book")
     */
    public void log(String actionName) {
        lock.lock();                              // blocare exclusiva
        try (PrintWriter pw = new PrintWriter(
                new FileWriter(AUDIT_FILE, true))) { // true = append mode
            String timestamp = LocalDateTime.now().format(formatter);
            pw.println(actionName + "," + timestamp);
        } catch (IOException e) {
            System.err.println("[AUDIT] Eroare la scriere: " + e.getMessage());
        } finally {
            lock.unlock();                        // eliberare garantata
        }
    }
}