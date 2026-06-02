package com.pao.laboratory13.exercise2;

public class Main {
    public static void main(String[] args) {
        int port = 9000;

        ChatServer server = new ChatServer(port);
        Thread serverThread = new Thread(server);
        serverThread.start();

        try { Thread.sleep(500); } catch (InterruptedException e) {}

        String[] scriptClient1 = {
                "AUTH alice",
                "OPEN",
                "SEND Salut, sunt Alice!",
                "HISTORY",
                "CLOSE"
        };

        String[] scriptClient2 = {
                "OPEN",
                "AUTH bob",
                "OPEN",
                "BROADCAST Salutare tuturor!",
                "SEND Sunt Bob!",
                "CLOSE"
        };

        ClientSimulator client1 = new ClientSimulator("Client-1", port, scriptClient1);
        ClientSimulator client2 = new ClientSimulator("Client-2", port, scriptClient2);

        Thread t1 = new Thread(client1);
        Thread t2 = new Thread(client2);

        t1.start();
        t2.start();
    }
}
