package ru.otus.java.basic.http.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    public static final int REQUEST_HANDLER_THREADS = 10;
    private int port;
    private Dispatcher dispatcher;
    private ExecutorService executorService;

    public HttpServer(int port) {
        this.port = port;
        this.dispatcher = new Dispatcher();
    }

    public void start() {
        executorService = Executors.newFixedThreadPool(REQUEST_HANDLER_THREADS);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту: " + port);
            do {
                Socket socket = serverSocket.accept();
                Thread request = new Thread(()->{
                    try {
                        byte[] buffer = new byte[8192];
                        int n = socket.getInputStream().read(buffer);
                        String rawRequest = new String(buffer, 0, n);
                        HttpRequest httpRequest = new HttpRequest(rawRequest);
                        httpRequest.printInfo(true);
                        dispatcher.execute(httpRequest, socket.getOutputStream());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            socket.close();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
                executorService.execute(request);
            } while (true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown(){
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }
}
