package ru.otus.java.basic.http.server;

public class Application {
    public static void main(String[] args) {
        HttpServer server = new HttpServer(8189);
        server.start();
        server.shutdown();
    }
}
