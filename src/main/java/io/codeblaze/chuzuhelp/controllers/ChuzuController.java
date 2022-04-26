package io.codeblaze.chuzuhelp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.codeblaze.chuzuhelp.dtos.MetaData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ChuzuController {

    private ObjectMapper mapper;

    public ChuzuController() {
        mapper = new ObjectMapper();
    }

    @PostMapping("/chuzu")
    public String Boop(@RequestBody JsonNode input) throws JsonProcessingException {
        MetaData metaData = mapper.treeToValue(input.path("destinationAddress").path("metaData"), MetaData.class);

        StringBuilder sb = new StringBuilder();

        input.path("destinationAddress").fields().forEachRemaining(field -> {
            if (field.getKey().equals("metaData")) return;

            List<String> list = field
                    .getValue()
                    .path(metaData.getArrayName())
                    .findValuesAsText(metaData.getArrayKeyName());

            sb.append(String.join(metaData.getSeparator(), list)).append(metaData.getSeparator());
        });

        return sb.toString().substring(0, sb.length() - 1);
    }

}
