package ru.otus.java.basic.http.server;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String rawRequest;
    private String uri;
    private HttpMethod method;
    private Map<String, String> parameters;
    private Map<String, String> properties;
    private String body;

    public String getRoutingKey() {
        return method + " " + uri;
    }

    public String getUri() {
        return uri;
    }

    public String getBody() {
        return body;
    }

    public HttpRequest(String rawRequest) {
        this.rawRequest = rawRequest;
        this.parse();
    }

    private Map<String, String> parseParameters(String uri) {
        Map<String, String> params = new HashMap<>();
        if (uri.contains("?")) {
            String[] elements = uri.split("[?]");
            this.uri = elements[0];
            String[] keysValues = elements[1].split("&");
            for (String o : keysValues) {
                String[] keyValue = o.split("=");
                params.put(keyValue[0], keyValue[1]);
            }
        }
        return params;
    }

    private Map<String, String> parseProperties(String rawRequest) {
        Map<String, String> props = new HashMap<>();
        String[] elements = rawRequest.split("\r\n");
        for (String element : elements) {
            if (element.contains(":")) {
                String[] keysValues = element.split("[:]\\s");
                props.put(keysValues[0], keysValues[1]);
            }
        }
        return props;
    }

    private void parse() {
        int startIndex = rawRequest.indexOf(' ');
        int endIndex = rawRequest.indexOf(' ', startIndex + 1);
        this.uri = rawRequest.substring(startIndex + 1, endIndex);
        String rawMethod = rawRequest.substring(0, startIndex);
        try{
            this.method = HttpMethod.valueOf(rawMethod);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Метод " + rawMethod + " не доступен");
        }
        this.parameters = parseParameters(uri);
        this.properties = parseProperties(rawRequest);
        if (method == HttpMethod.POST) {
            this.body = rawRequest.substring(
                    rawRequest.indexOf("\r\n\r\n") + 4
            );
        }
    }

    public boolean containsParameter(String key) {
        return parameters.containsKey(key);
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public String printInfo(boolean showRawRequest) {
        StringBuilder httpRequest = new StringBuilder();
        httpRequest.append("uri: " + uri)
                .append(System.lineSeparator() + "method: " + method)
                .append(System.lineSeparator() + "body: " + body)
                .append(System.lineSeparator() + "properties: " + properties);
        if (showRawRequest) {
            httpRequest.append(System.lineSeparator() + rawRequest);
        }
        return httpRequest.toString();
    }
}
