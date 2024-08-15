package ru.otus.java.basic.http.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java.basic.http.server.app.ItemsRepository;
import ru.otus.java.basic.http.server.processors.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class Dispatcher {
    public static final Logger logger = LoggerFactory.getLogger(Dispatcher.class.getName());

    private Map<String, RequestProcessor> processors;
    private RequestProcessor defaultNotFoundRequestProcessor;
    private RequestProcessor defaultInternalServerErrorProcessor;
    private CommonProcessor commonProcessor;

    private ItemsRepository itemsRepository;

    public Dispatcher() {
        this.itemsRepository = new ItemsRepository();

        this.processors = new HashMap<>();
        this.processors.put("GET /", new HelloWorldRequestProcessor());
        this.processors.put("GET /another", new AnotherHelloWorldRequestProcessor());
        this.processors.put("GET /calculator", new CalculatorRequestProcessor());
        this.processors.put("GET /items", new GetAllItemsProcessor(itemsRepository));
        this.processors.put("POST /items", new CreateNewItemProcessor(itemsRepository));
        this.processors.put("DELETE /items", new DeleteItemProcessor(itemsRepository));

        this.defaultNotFoundRequestProcessor = new DefaultNotFoundRequestProcessor();
        this.defaultInternalServerErrorProcessor = new DefaultInternalServerErrorRequestProcessor();
        this.commonProcessor = new CommonProcessor();
    }

    public void execute(HttpRequest request, OutputStream out) throws IOException {
        try {
            if (!processors.containsKey(request.getRoutingKey())) {
                defaultNotFoundRequestProcessor.execute(request, out);
                return;
            }
            processors.get(request.getRoutingKey()).execute(request, out);
        } catch (BadRequestException e) {
            DefaultErrorDto defaultErrorDto = new DefaultErrorDto("CLIENT_DEFAULT_ERROR", e.getMessage());
            request.getHttpResponse().setDto(defaultErrorDto);
            executeError(request, out, e);
            logger.error(e.getMessage(), e);
        }
    }

    public void executeError(HttpRequest request, OutputStream out, BadRequestException e) throws IOException {
        fillHeaderHttpResponse(request.getHttpResponse(), e);
        commonProcessor.execute(request, out);
    }

    public void executeError(OutputStream out, BadRequestException e) throws IOException {
        HttpResponse httpResponse = new HttpResponse();
        fillHeaderHttpResponse(httpResponse, e);
        commonProcessor.execute(httpResponse, out);
    }

    private void fillHeaderHttpResponse(HttpResponse httpResponse, BadRequestException e) {
        if(e instanceof HttpRequestException) {
            HttpRequestException re = (HttpRequestException) e;
            httpResponse.getHeadersResponse().setStatusCode(re.getStatusCode());
            httpResponse.getHeadersResponse().setReasonPhrase(re.getMessage());

            switch (re.getStatusCode()) {
                //TODO Add header-processing for other status codes
                case HttpURLConnection.HTTP_BAD_METHOD:
                case HttpURLConnection.HTTP_NOT_IMPLEMENTED: {
                    httpResponse.getHeadersResponse().addHeader("ALLOW", HttpMethod.GET.name() + ", " +
                            HttpMethod.POST.name() + ", " +
                            HttpMethod.DELETE.name());
                } break;
            }
        } else {
            httpResponse.getHeadersResponse().setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
            httpResponse.getHeadersResponse().setReasonPhrase("Bad Request");
        }
    }
}
