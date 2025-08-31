package org.test.singletons;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperCreator {

    private ObjectMapperCreator(){
    }

    public static ObjectMapper objectMapper(){
        return ObjectMapperHelper.OBJECT_MAPPER;
    }

    private static class ObjectMapperHelper{
        private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    }



}
