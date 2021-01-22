package com.digitalwallet.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.util.Base64;

public class Util {

	private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);
	
	public static String encode(String name) {
		String hashkey = "";
		try {
			hashkey = new String(Base64.encode(name.getBytes()));
		} catch (Exception e) {
			LOGGER.error("Exception ocuured while genetating hash for fileName:{}", name, e);
		}
		return hashkey;

	}
}
