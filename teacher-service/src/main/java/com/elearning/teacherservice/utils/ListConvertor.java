package com.elearning.teacherservice.utils;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.List;

@Configuration
@Converter
public class ListConvertor implements AttributeConverter<List, String> {

    // This method encrypts the user sensitive info into the database.
    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(List courses) {
        if (courses == null)
            return null;
        return String.join(",", courses);
    }


    // This method decrypts the encrypted info
    @SneakyThrows
    @Override
    public List convertToEntityAttribute(String dbData) {
        if (dbData == null)
            return null;
        return  Arrays.asList(dbData.split(","));
    }
}
