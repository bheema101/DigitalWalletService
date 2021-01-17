package com.digitalwallet.converter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

public class SetConverter implements DynamoDBTypeConverter<String, Set<String>>{

	@Override
	public String convert(Set<String> fileNames) {
		int size = fileNames.size();
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
		return names;
	}

}
