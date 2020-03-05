package com.onlineinteract.fileserver.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@EnableAutoConfiguration
public class CustomerController {

	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value = "/fetchCustomer/{customerId}")
	@ResponseBody
	public ResponseEntity<String> fetchCustomer(@PathVariable String customerId) {
		System.out.println("\nFetching customer with customer ID: " + customerId);

		Map<String, String> customer = new HashMap<String, String>();
		customer.put("CustomerId", customerId);
		customer.put("AccountNumber", "12345678");
		customer.put("Forename", "Angela");
		customer.put("Surname", "Beaton");
		customer.put("DOB", "22/04/78");
		customer.put("Address1", "123-456-789");
		customer.put("Address2", "123-456-789");
		customer.put("City", "123-456-789");
		customer.put("Postcode", "123-456-789");
		customer.put("SIN", "123-456-789");

		ObjectMapper mapper = new ObjectMapper();
		String customerJson = null;
		try {
			customerJson = mapper.writeValueAsString(customer);
		} catch (JsonProcessingException e) {
			System.out.println("There was a problem converting customer map to json String: " + e.getOriginalMessage());
		}

		return new ResponseEntity<>(customerJson, HttpStatus.OK);
	}
}
