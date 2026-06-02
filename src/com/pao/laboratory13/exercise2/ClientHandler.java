package com.pao.laboratory13.exercise2;

import com.pao.laboratory13.exercise1.ProtocolEngine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    static int id_cnt;
    private int clientId;
    private ProtocolEngine engine;
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        engine = new ProtocolEngine();
        clientId = id_cnt++;
    }

    @Override
    public void run() {
        System.out.println("[CLIENT-" + clientId + "] Conectat din " + socket.getInetAddress());
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        ) {
            String cmd;
            while ((cmd = in.readLine()) != null) {
                String rez = engine.procesareCmd(cmd);
                System.out.println("[CLIENT-" + clientId + "] >> " + cmd + "  =>  " + rez);
                if (rez != null) {
                    out.println(rez);
                }
            }
        }
        catch (Exception e) {
            System.out.println("[CLIENT-" + clientId + "] >> " + e.getMessage());
        }
        finally {
            try {
                if (!socket.isClosed()) {
                    socket.close();
                }
                System.out.println("[CLIENT-" + clientId + "] Deconectat");
            }
            catch (Exception e) {
                System.out.println("[CLIENT-" + clientId + "] >> " + e.getMessage());
            }
        }
    }
}
