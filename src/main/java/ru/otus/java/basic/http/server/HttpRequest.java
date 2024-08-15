package ru.otus.java.basic.http.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    public static final Logger logger = LoggerFactory.getLogger(HttpRequest.class.getName());

    private String rawRequest;
    private String uri;
    private HttpMethod method;
    private Map<String, String> parameters;
    private Map<String, String> properties;
    private String body;
    private HttpResponse httpResponse = new HttpResponse();

    public HttpRequest(String rawRequest) {
        this.rawRequest = rawRequest;
        this.parse();
    }

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }

    public String getRoutingKey() {
        return method + " " + uri;
    }

    public String getUri() {
        return uri;
    }

    public String getBody() {
        return body;
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
            throw new HttpRequestException(HttpURLConnection.HTTP_NOT_IMPLEMENTED, "Not Implemented");
        }
        if(!method.isSupported()) {
            throw new HttpRequestException(HttpURLConnection.HTTP_BAD_METHOD, "Method Not Allowed");
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

    public void printInfo() {
        StringBuilder httpRequest = new StringBuilder();
        httpRequest.append("uri: ").append(uri).append(System.lineSeparator())
                .append("method: ").append(method).append(System.lineSeparator())
                .append("body: ").append(body).append(System.lineSeparator())
                .append("properties: ").append(properties);
        logger.info(httpRequest.toString());
        logger.debug(rawRequest);
    }
}
