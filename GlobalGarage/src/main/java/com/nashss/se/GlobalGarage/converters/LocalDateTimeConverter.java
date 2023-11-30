package com.nashss.se.GlobalGarage.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.LocalDateTime;

/**
 * Custom converter for LocalDateTime, as DynamoDB doesn't support LocalDateTime natively.
 */
public class LocalDateTimeConverter implements DynamoDBTypeConverter<String, LocalDateTime> {

    @Override
    public String convert(final LocalDateTime time) {
        return time != null ? time.toString() : null;
    }

    @Override
    public LocalDateTime unconvert(final String stringValue) {
        return stringValue != null ? LocalDateTime.parse(stringValue) : null;
    }
}
