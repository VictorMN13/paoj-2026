package com.pao.laboratory13.exercise2;

import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer implements Runnable{
    private final int port;

    public ChatServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("[SERVER] Listening on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket);
                Thread clientThread = new Thread(handler);
                clientThread.start();
            }

        } catch (Exception e) {
            System.err.println("[SERVER] Eroare la pornirea serverului: " + e.getMessage());
        }
    }
}
