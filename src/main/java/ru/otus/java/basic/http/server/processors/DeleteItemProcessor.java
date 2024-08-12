package ru.otus.java.basic.http.server.processors;

import com.google.gson.Gson;
import ru.otus.java.basic.http.server.DefaultErrorDto;
import ru.otus.java.basic.http.server.DefaultRequestDto;
import ru.otus.java.basic.http.server.HttpMethod;
import ru.otus.java.basic.http.server.HttpRequest;
import ru.otus.java.basic.http.server.app.Item;
import ru.otus.java.basic.http.server.app.ItemsRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class DeleteItemProcessor implements RequestProcessor{
    private ItemsRepository itemsRepository;

    public DeleteItemProcessor(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    @Override
    public void execute(HttpRequest request, OutputStream out) throws IOException {
        String response;
        long id = Long.parseLong(request.getParameter("id"));
        Item item = itemsRepository.get(id);
        Gson gson = new Gson();
        if(item != null) {
            itemsRepository.remove(item);
            DefaultRequestDto requestDto = new DefaultRequestDto("OK");
            requestDto.setMethod(HttpMethod.DELETE);
            String respJson = gson.toJson(requestDto);
            response = "" +
                    "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: application/json\r\n" +
                    "\r\n" +
                    respJson;
        } else {
            DefaultErrorDto defaultErrorDto  = new DefaultErrorDto("NO_CONTENT", "объект id=" + id + " не найден" );
            defaultErrorDto.setMethod(HttpMethod.DELETE);
            String errorJson = gson.toJson(defaultErrorDto);
            response = "" +
                    "HTTP/1.1 404 Not Found\r\n" +
                    "Content-Type: application/json\r\n" +
                    "\r\n" +
                    errorJson;
        }
        out.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
