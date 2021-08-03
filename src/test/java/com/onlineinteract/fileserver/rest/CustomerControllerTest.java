package com.onlineinteract.fileserver.rest;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

public class CustomerControllerTest {

	private static CustomerController customerController;
	private static final String CUSTOMER_JSON = "{\"Forename\":\"Alex\",\"DOB\":\"22/04/76\",\"Address2\":\"Ingles\",\"SIN\":\"123-456-789\",\"CustomerId\":\"1\",\"Address1\":\"142 Potter Way\",\"City\":\"Surrey\",\"Surname\":\"Beaton\",\"Postcode\":\"SU12 3HG\",\"AccountNumber\":\"12345678\"}";
	private static final String EMPTY_CUSTOMER_JSON = "{}";

	@BeforeClass
	public static void setup() {
		customerController = new CustomerController();
	}

	@Test
	public void assembleCustomerTest() {
		Map<String, String> customer = new HashMap<String, String>();
		customer.put("CustomerId", "1");
		customer.put("AccountNumber", "12345678");
		customer.put("Forename", "Alex");
		customer.put("Surname", "Beaton");
		customer.put("DOB", "22/04/76");
		customer.put("Address1", "142 Potter Way");
		customer.put("Address2", "Ingles");
		customer.put("City", "Surrey");
		customer.put("Postcode", "SU12 3HG");
		customer.put("SIN", "123-456-789");

		String customerJson = customerController.extractCustomer(customer);
		assertEquals(CUSTOMER_JSON, customerJson);
	}

	@Test
	public void assembleEmptyCustomerTest() {
		Map<String, String> customer = new HashMap<String, String>();
		String customerJson = customerController.extractCustomer(customer);
		assertEquals(EMPTY_CUSTOMER_JSON, customerJson);
	}

}
