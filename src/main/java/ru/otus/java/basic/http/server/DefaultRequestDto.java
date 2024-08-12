package ru.otus.java.basic.http.server;

import java.time.LocalDateTime;

public class DefaultRequestDto {
    protected String code;
    protected String date;
    protected HttpMethod method;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public DefaultRequestDto() {
    }

    public DefaultRequestDto(String code) {
        this.code = code;
        this.date = LocalDateTime.now().toString();
    }
}
