package ru.otus.java.basic.http.server;

import java.io.IOException;
import java.net.Socket;

public class HttpHandler {
    private Socket socket;

    public HttpHandler(Socket socket, Dispatcher dispatcher) {
        this.socket = socket;
        new Thread(() -> {
            try {
                byte[] buffer = new byte[8192];
                int n = socket.getInputStream().read(buffer);
                String rawRequest = new String(buffer, 0, n);
                HttpRequest request = new HttpRequest(rawRequest);
                request.printInfo(true);
                dispatcher.execute(request, socket.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                shutdown();
            }
        }).start();
    }

    public boolean shutdown() {
        try {
            if (socket != null) {
                socket.close();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
