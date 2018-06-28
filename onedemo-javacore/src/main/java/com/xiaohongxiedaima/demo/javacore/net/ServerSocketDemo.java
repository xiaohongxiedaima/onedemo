package com.xiaohongxiedaima.demo.javacore.net;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

public class ServerSocketDemo implements Runnable {

    public static void main(String[] args) {
        Executors.newSingleThreadExecutor().execute(new ServerSocketDemo());
    }

    public void run() {
        try {
            ServerSocket ss = new ServerSocket(8080);
            while (!Thread.interrupted())
                new Thread(new Handler(ss.accept())).start();
        } catch (Exception ex) { }
    }

}

class Handler implements Runnable {
    final Socket socket;
    Handler(Socket s) { socket = s; }
    public void run() {
        try {
            byte[] input = new byte[1024];
            socket.getInputStream().read(input);
            byte[] output = process(input);
            socket.getOutputStream().write(output);
        } catch (Exception ex) { }
    }
    private byte[] process(byte[] cmd) { return cmd; }
}