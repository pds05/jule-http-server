package ru.otus.java.basic.http.server.processors;

import com.google.gson.Gson;
import ru.otus.java.basic.http.server.HttpRequest;
import ru.otus.java.basic.http.server.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class CommonProcessor implements RequestProcessor {

    @Override
    public void execute(HttpRequest request, OutputStream out) throws IOException {
        HttpResponse httpResponse = request.getHttpResponse();
        execute(httpResponse, out);
    }

    public void execute(HttpResponse httpResponse, OutputStream out) throws IOException {
        StringBuilder builder = new StringBuilder();

        builder.append("HTTP/1.1 ")
                .append(httpResponse.getHeadersResponse().getStatusCode())
                .append(" ")
                .append(httpResponse.getHeadersResponse().getReasonPhrase())
                .append(System.lineSeparator());
        if (httpResponse.getDto() != null) {
            builder.append("Content-Type: application/json").append(System.lineSeparator());
            builder.append(new Gson().toJson(httpResponse.getDto()));
        } else {
            builder.append("Content-Type: text/html").append(System.lineSeparator());
            builder.append(System.lineSeparator());
            if(httpResponse.getMessage() != null) {
                builder.append(httpResponse.getMessage());
            }
        }
        out.write(builder.toString().getBytes(StandardCharsets.UTF_8));
    }
}
