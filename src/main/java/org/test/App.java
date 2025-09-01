package org.test;

import com.fasterxml.jackson.databind.JsonNode;
import org.test.singletons.ObjectMapperCreator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class App{
    public static void main(String[] args) throws IOException {
        var mapper = ObjectMapperCreator.objectMapper();
        JsonNode rootNode = mapper.readTree(Files.readAllBytes(Path.of("smartcommit-config.json")));
        JsonNode identNode = rootNode.path("person-ident");
        PersonIdentWrapper wrapper = mapper.convertValue(identNode, PersonIdentWrapper.class);
        System.out.println(wrapper.name());
        System.out.println(wrapper.email());
    }
}




