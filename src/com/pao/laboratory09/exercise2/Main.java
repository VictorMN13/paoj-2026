package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;
import com.pao.laboratory09.exercise1.Tranzactie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        sc.nextLine();
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            for (int i = 0; i < n; i++) {
                String[] tokens = sc.nextLine().trim().split(" ");
                int id = Integer.parseInt(tokens[0]);
                double suma = Double.parseDouble(tokens[1]);
                String data = tokens[2];
                byte tip = (byte) (TipTranzactie.valueOf(tokens[3]).ordinal());
                byte status = 0;

                dos.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(id).array());
                dos.write(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putDouble(suma).array());

                byte[] dataBytes = data.getBytes("ASCII");
                byte[] dataPadded = new byte[10];
                for (int j = 0; j < 10; j++) {
                    dataPadded[j] = (j < dataBytes.length) ? dataBytes[j] : (byte) ' ';
                }
                dos.write(dataPadded);

                dos.writeByte(tip);
                dos.writeByte(status);
                dos.write(new byte[8]);
            }
        } catch (IOException e) {
            System.err.println("Eroare la scrierea inițială: " + e.getMessage());
            return;
        }

        try (RandomAccessFile raf = new RandomAccessFile(OUTPUT_FILE, "rw")) {
            while (sc.hasNext()) {
                String cmd = sc.next();

                switch (cmd) {
                    case "UPDATE" -> {
                        int idx = sc.nextInt();
                        String statusStr = sc.next();

                        byte statusCode = (byte) Status.valueOf(statusStr).ordinal();

                        raf.seek((long) idx * RECORD_SIZE + 23);
                        raf.writeByte(statusCode);

                        System.out.println("Updated [" + idx + "]: " + statusStr);
                    }

                    case "READ" -> {
                        int idx = sc.nextInt();
                        printRecord(raf, idx);
                    }

                    case "PRINT_ALL" -> {
                        for (int i = 0; i < n; i++) {
                            printRecord(raf, i);
                        }
                    }

                    default -> sc.nextLine(); // Ignorăm comenzile necunoscute
                }
            }
        } catch (Exception e) {
            System.err.println("Eroare la accesarea fișierului: " + e.getMessage());
        }

        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data tip)
        // 2. Scrie toate înregistrările în OUTPUT_FILE cu DataOutputStream (format binar, RECORD_SIZE=32 bytes/înreg.)
        //    - bytes 0-3:   id (int, little-endian via ByteBuffer)
        //    - bytes 4-11:  suma (double, little-endian via ByteBuffer)
        //    - bytes 12-21: data (String, 10 chars ASCII, paddat cu spații la dreapta)
        //    - byte 22:     tip (0=CREDIT, 1=DEBIT)
        //    - byte 23:     status (0=PENDING, 1=PROCESSED, 2=REJECTED)
        //    - bytes 24-31: padding (zerouri)
        // 3. Procesează comenzile din stdin până la EOF cu RandomAccessFile:
        //    - READ idx       → seek(idx * RECORD_SIZE), citește și afișează înregistrarea
        //    - UPDATE idx ST  → seek(idx * RECORD_SIZE + 23), scrie noul status (0/1/2)
        //                       afișează "Updated [idx]: STATUS"
        //    - PRINT_ALL      → citește și afișează toate înregistrările
        //
        // Format linie output:
        //   [idx] id=<id> data=<data> tip=<CREDIT|DEBIT> suma=<suma:.2f> RON status=<STATUS>

    }

    private static void printRecord(RandomAccessFile raf, int idx) throws Exception {
        raf.seek((long) idx * RECORD_SIZE);

        byte[] buffer = new byte[RECORD_SIZE];
        raf.read(buffer);

        ByteBuffer bb = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);

        int id = bb.getInt();
        double suma = bb.getDouble();

        byte[] dataBytes = new byte[10];
        bb.get(dataBytes);
        String data = new String(dataBytes, "ASCII").trim();

        byte tipCode = bb.get();
        String tip = TipTranzactie.values()[tipCode].toString();

        byte statusCode = bb.get();
        String status = Status.values()[statusCode].toString();

        System.out.printf(Locale.US, "[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s\n",
                idx, id, data, tip, suma, status);
    }
}
