package ru.otus.java.basic.http.server;

public class HttpRequestException extends BadRequestException{
    private int statusCode;
    private String reasonPhrase;

    public HttpRequestException(int statusCode, String reasonPhrase) {
        super(reasonPhrase);
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }
}
