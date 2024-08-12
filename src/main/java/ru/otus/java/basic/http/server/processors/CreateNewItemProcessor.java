package ru.otus.java.basic.http.server.processors;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java.basic.http.server.BadRequestException;
import ru.otus.java.basic.http.server.HttpRequest;
import ru.otus.java.basic.http.server.app.Item;
import ru.otus.java.basic.http.server.app.ItemsRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class CreateNewItemProcessor implements RequestProcessor {
    public static final Logger logger = LoggerFactory.getLogger(CreateNewItemProcessor.class.getName());
    private ItemsRepository itemsRepository;

    public CreateNewItemProcessor(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    @Override
    public void execute(HttpRequest request, OutputStream out) throws IOException {
        try {
            Gson gson = new Gson();
            Item item = itemsRepository.add(gson.fromJson(request.getBody(), Item.class));
            String itemJson = gson.toJson(item);
            String response = "" +
                    "HTTP/1.1 201 Created\r\n" +
                    "Content-Type: application/json\r\n" +
                    "\r\n" +
                    itemJson;
            out.write(response.getBytes(StandardCharsets.UTF_8));
        } catch (JsonParseException e) {
            logger.error(e.getMessage(), e);
            throw new BadRequestException("Некорректный формат входящего JSON объекта");
        }
    }
}
