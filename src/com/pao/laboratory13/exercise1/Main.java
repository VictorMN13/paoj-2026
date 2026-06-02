package com.pao.laboratory13.exercise1;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // TODO: messaging server based on System.in command parsing.

        Scanner sc = new Scanner(System.in);
        String cmd = "";
        int n = sc.nextInt();
        sc.nextLine();
        ProtocolEngine engine = new ProtocolEngine();

        for (int i = 1; i <= n; i++) {
            cmd = sc.nextLine();
            String rez = engine.procesareCmd(cmd);
            System.out.println(rez);
        }

//        System.out.println("TODO: implement laboratory13 exercise1");
    }
}
