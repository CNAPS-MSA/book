package com.skcc.book.domain.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skcc.book.domain.BookReservation;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Set;

@Converter
public class BookReservationConverter implements AttributeConverter<Set<BookReservation>, String> {
    private ObjectMapper om = new ObjectMapper();


    @Override
    public String convertToDatabaseColumn(Set<BookReservation> bookReservations) {
        try {
            return om.writeValueAsString(bookReservations);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public Set<BookReservation> convertToEntityAttribute(String s) {
        try {
            return om.readValue(s, new TypeReference<Set<BookReservation>>() { });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
