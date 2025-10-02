package com.kraiwit.xogame.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class StringArrayConverter implements AttributeConverter<String[][], String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(String[][] attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert String[][] to JSON", e);
        }
    }

    @Override
    public String[][] convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(dbData, String[][].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON to String[][]", e);
        }
    }
}