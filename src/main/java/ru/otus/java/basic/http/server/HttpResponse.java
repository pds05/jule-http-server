package ru.otus.java.basic.http.server;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private HeadersResponse headersResponse = new HeadersResponse();
    private DefaultRequestDto dto;
    private String message;

    public HttpResponse(DefaultRequestDto dto) {
        this.dto = dto;
    }

    public HttpResponse(String message) {
        this.message = message;
    }

    public HttpResponse() {
    }

    public HeadersResponse getHeadersResponse() {
        return headersResponse;
    }

    public DefaultRequestDto getDto() {
        return dto;
    }

    public void setDto(DefaultRequestDto dto) {
        this.dto = dto;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class HeadersResponse {
        private int statusCode = 200;
        private String reasonPhrase = "OK";
        private Map<String, String> headers = new HashMap<String, String>();

        public HeadersResponse() {
        }

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public String getReasonPhrase() {
            return reasonPhrase;
        }

        public void setReasonPhrase(String reasonPhrase) {
            this.reasonPhrase = reasonPhrase;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public void addHeader(String headerName, String headerValue) {
            headers.put(headerName, headerValue);
        }
    }
}
