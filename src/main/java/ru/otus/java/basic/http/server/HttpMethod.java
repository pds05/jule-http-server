package ru.otus.java.basic.http.server;

public enum HttpMethod {
    GET(true), POST(true), DELETE(true), HEAD(false), OPTIONS(false), PUT(false), TRACE(false), CONNECT(false), PATCH(false), ;

    private boolean isSupported;

    HttpMethod(boolean isSupported) {
        this.isSupported = isSupported;
    }

    public boolean isSupported() {
        return isSupported;
    }
}
