package com.onlineinteract.fileserver.crud.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.onlineinteract.fileserver.domain.Customer;

@Repository
public class CustomerCrudRepository {
	private Map<String, Customer> store = new HashMap<>();

	public void addCustomer(String id, Customer customer) {
		store.put(id, customer);
	}

	public Customer getCustomer(String id) {
		return store.get(id);
	}
	
	public Map<String, Customer> getAllCustomers() {
		return store;
	}
	
	public int customerCount() {
		return store.size();
	}
}
