package org.jharkendar.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;

public class JsonMapper {
    private JsonMapper() {

    }

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(Object object) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Could not parse \"" + object.toString() + "\" to json");
        }
    }

    public static <T> T fromJson(String jsonText, Class<T> classInfo) {
        try {
            return objectMapper.reader().readValue(jsonText, classInfo);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not parse \"" + jsonText + "\" to class " + classInfo.getSimpleName());
        }
    }

}
