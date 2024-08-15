package ru.otus.java.basic.http.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    public static final Logger logger = LoggerFactory.getLogger(HttpServer.class.getName());

    private int port;
    private Dispatcher dispatcher;

    public HttpServer(int port) {
        this.port = port;
        this.dispatcher = new Dispatcher();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Сервер запущен на порту: " + port);
            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    byte[] buffer = new byte[8192];
                    int n = socket.getInputStream().read(buffer);
                    if (n < 1) {
                        continue;
                    }
                    String rawRequest = new String(buffer, 0, n);
                    try {
                        HttpRequest request = new HttpRequest(rawRequest);
                        request.printInfo();
                        dispatcher.execute(request, socket.getOutputStream());
                    } catch (BadRequestException bre) {
                        dispatcher.executeError(socket.getOutputStream(), bre);
                    } catch (Exception e) {
                        dispatcher.executeError(socket.getOutputStream(), new HttpRequestException(HttpURLConnection.HTTP_INTERNAL_ERROR, "Internal Server Error"));
                    }
                }
            }
        } catch (IOException ioe) {
            logger.error("Сетевая ошибка привела к остановке сервера: " + ioe.getMessage(), ioe);
        } catch (Exception e) {
            logger.error("Непредвиденная ошибка привела к остановке сервера: " + e.getMessage(), e);
        }
    }
}
