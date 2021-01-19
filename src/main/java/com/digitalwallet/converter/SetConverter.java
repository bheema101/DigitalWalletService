package com.digitalwallet.converter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.digitalwallet.rest.DigitalWalletController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetConverter implements DynamoDBTypeConverter<String, Set<String>>{

	private static final Logger LOGGER = LoggerFactory.getLogger(SetConverter.class);
	
	

	@Override
	public String convert(Set<String> fileNames) {
		int size = fileNames.size();
		LOGGER.info("conver from set {} to string",fileNames);
		StringBuilder str = new StringBuilder("[");
		int i =1;
		if(fileNames != null) {
			for(String srt : fileNames) {
				
				str.append(srt);
				if(size>i) {
				    str.append(",");
				}
				i++;
				
			}
		}
		
		
		str.append("]");

		return str.toString();
	}

	@Override
	public Set<String> unconvert(String s) {
	String substring = s.substring(1,s.length()-1);
	
	Set<String> names = new HashSet<>();
	
	if(substring != null) {
		String [] filenames = substring.split(",");
		names.addAll(Arrays.asList(filenames));
	}
		LOGGER.info("unconvert from String  {} to set :{}",s,names);
		return names;
	}

}
