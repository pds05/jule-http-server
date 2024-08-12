package ru.otus.java.basic.http.server;

public class DefaultErrorDto extends DefaultRequestDto {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DefaultErrorDto() {
    }

    public DefaultErrorDto(String code, String message) {
        super(code);
        this.message = message;
    }
}
