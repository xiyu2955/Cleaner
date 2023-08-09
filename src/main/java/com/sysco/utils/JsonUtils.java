package com.sysco.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public abstract class JsonUtils {
    public static final ObjectMapper OBJECT_MAPPER = JsonMapper.builder().addModule(new JavaTimeModule()).build();

    /**
     * Converts a Java object to a JSON string.
     *
     * @param object the Java object to convert
     * @return the JSON string representation of the Java object
     */
    public static String toJson(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Converts a Java object to a JSON string.
     *
     * @param object the Java object to convert
     * @return the JSON string representation of the Java object
     */
    public static String toJsonWithFilter(Object object, FilterProvider provider) {
        try {
            return OBJECT_MAPPER.writer(provider).writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Converts a JSON string to a Java object.
     *
     * @param json  the JSON string to convert
     * @param clazz the class of the Java object to return
     * @param <T>   the type of the Java object to return
     * @return the Java object representation of the JSON string
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Converts a JSON string to a Java object.
     *
     * @param json          the JSON string to convert
     * @param TypeReference the class of the Java object to return
     * @param <T>           the type of the Java object to return
     * @return the Java object representation of the JSON string
     */
    public static <T> T fromJson(String json, TypeReference<T> TypeReference) {
        try {
            return OBJECT_MAPPER.readValue(json, TypeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
