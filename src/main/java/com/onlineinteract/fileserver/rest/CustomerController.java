//package com.onlineinteract.fileserver.rest;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Random;
//
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.onlineinteract.fileserver.CustomerServiceApp;
//
//@Controller
//@EnableAutoConfiguration
//public class CustomerController {
//	@SuppressWarnings("rawtypes")
//	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value = "/fetchCustomer/{customerId}")
//	@ResponseBody
//	public ResponseEntity<String> fetchCustomer(@PathVariable String customerId,
//			@RequestHeader HttpHeaders incomingHeaders) throws SQLException {
//		System.out.println("\nFetching customer with customer ID: " + customerId);
//
//		System.out.println("Headers: ");
//		Iterator it = incomingHeaders.entrySet().iterator();
//		while (it.hasNext()) {
//			Map.Entry pair = (Map.Entry) it.next();
//			System.out.println(pair.getKey() + " = " + pair.getValue());
//		}
//		
//		Random random = new Random();
//		int userId = random.nextInt(5 - 1 + 1) + 1;
//		
//		Statement statement = CustomerServiceApp.databaseConnection.createStatement();
//		statement.setQueryTimeout(60);
//		statement.setFetchSize(1000);
//		ResultSet rs = statement.executeQuery("SELECT * from users where user_id = " + userId + ";");
//		rs.next();
//
//		Map<String, String> customer = new HashMap<String, String>();
//		customer.put("CustomerId", rs.getString("user_id"));
//		customer.put("AccountNumber", "12345678");
//		customer.put("Forename", rs.getString("name"));
//		customer.put("Surname", "Beaton");
//		customer.put("DOB", "22/04/78");
//		customer.put("Address1", "123-456-789");
//		customer.put("Address2", "123-456-789");
//		customer.put("City", "123-456-789");
//		customer.put("Postcode", "123-456-789");
//		customer.put("SIN", "123-456-789");
//		
//		rs.close();
//		statement.close();
//
//		ObjectMapper mapper = new ObjectMapper();
//		String customerJson = null;
//		try {
//			customerJson = mapper.writeValueAsString(customer);
//			System.out.println("Returning: " + customerJson);
//		} catch (JsonProcessingException e) {
//			System.out.println("There was a problem converting customer map to json String: " + e.getOriginalMessage());
//		}
//
//		return new ResponseEntity<>(customerJson, HttpStatus.OK);
//	}
//}
