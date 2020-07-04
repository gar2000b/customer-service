package com.onlineinteract.fileserver.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParser {

	public static String toJson(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		String json = null;
		try {
			json = mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			System.out.println("There was a problem converting customer map to json String: " + e.getOriginalMessage());
		}
		return json;
	}
}
