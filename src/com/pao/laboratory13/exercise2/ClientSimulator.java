package com.pao.laboratory13.exercise2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSimulator implements Runnable {
    private final String clientName;
    private final int serverPort;
    private final String[] commands;

    public ClientSimulator(String clientName, int serverPort, String[] commands) {
        this.clientName = clientName;
        this.serverPort = serverPort;
        this.commands = commands;
    }

    @Override
    public void run() {
        try (
                Socket socket = new Socket("localhost", serverPort);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            for (String cmd : commands) {
                out.println(cmd);
                String response = in.readLine();
                Thread.sleep(300);
            }

        } catch (Exception e) {
            System.err.println("[" + clientName + "] Eroare: " + e.getMessage());
        }
    }
}
