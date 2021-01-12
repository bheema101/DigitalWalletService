package com.digitalwallet.converter;

import java.time.LocalDateTime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

public class LocalDateTimeToStringTypeConverter implements DynamoDBTypeConverter<String, LocalDateTime> {

	@Override
	public String convert(LocalDateTime dateTime) {
		
		 return dateTime.toString();
	}

	@Override
	public LocalDateTime unconvert(String datetime) {
		
		return LocalDateTime.parse(datetime);
	}

}
