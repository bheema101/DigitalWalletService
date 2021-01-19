package com.digitalwallet.converter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {
	
	private static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("UTC");
	
	public LocalDateTimeSerializer() {
        super(LocalDateTime.class);

    }

   
	@Override
	public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		// TODO Auto-generated method stub
		  if (value != null) {
	            final long mills = value.atZone(DEFAULT_ZONE_ID).toInstant().toEpochMilli();
	            gen.writeNumber(mills);
	        } else {
	        	gen.writeNull();
	        }
	}

}
