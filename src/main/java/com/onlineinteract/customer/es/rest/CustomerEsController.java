package com.onlineinteract.customer.es.rest;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.onlineinteract.customer.crud.repository.CustomerCrudRepository;
import com.onlineinteract.customer.domain.Asset;
import com.onlineinteract.customer.domain.Customer;
import com.onlineinteract.customer.domain.Reference;
import com.onlineinteract.customer.es.events.CustomerCreatedEvent;
import com.onlineinteract.customer.es.events.CustomerUpdatedEvent;
import com.onlineinteract.customer.es.repository.EventStoreRepository;
import com.onlineinteract.customer.es.utility.CustomerUtility;
import com.onlineinteract.customer.utility.JsonParser;

@Controller
@EnableAutoConfiguration
@RequestMapping("/es")
public class CustomerEsController {

	@Autowired
	CustomerCrudRepository customerRepository;

	@Autowired
	EventStoreRepository eventStoreRepository;

	/**
	 * createCustomer()
	 * 
	 * curl -i -H "Content-Type:application/json" -X POST --data '{"id":"1234",
	 * "forename":"Gary", "surname":"Black", "dob":"01/01/1980", "address1":"12 Elf
	 * Avenue", "address2":"", "city":"Laplando", "postcode":"X1M2A5",
	 * "sin":"9876543210", "references":[{"forename":"Daniel", "surname":"Lester",
	 * "telephoneNumber":"416-123-4567", "sin":"8765432109"},{"forename":"Linda",
	 * "surname":"Hamilton", "telephoneNumber":"416-123-4563", "sin":"6543210978"}],
	 * "assets":[{"name":"House", "value":"$400,000"},{"name":"Vehicle",
	 * "value":"$35,000"}]}' http://localhost:9082/es/customer
	 * 
	 * @param customer
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json", value = "/customer")
	@ResponseBody
	public ResponseEntity<String> createCustomer(@RequestBody Customer customer) {
		System.out.println("*** createCustomer() called ***");
		String customerId = UUID.randomUUID().toString();
		customer.setId(customerId);
		CustomerCreatedEvent customerCreatedEvent = new CustomerCreatedEvent(customer);
		customerCreatedEvent.setId(String.valueOf(customerCreatedEvent.getCreated().getTime()));
		eventStoreRepository.publishEvent(customerCreatedEvent, customerId);
		return new ResponseEntity<>("createCustomer(): " + JsonParser.toJson(customer), HttpStatus.OK);
	}

	/**
	 * getCustomer()
	 * 
	 * curl -i
	 * http://localhost:9082/es/customer/e90d1e9b-7ebc-4ae8-a4d4-9a041c637849
	 * 
	 * @param customerId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value = "/customer/{customerId}")
	@ResponseBody
	public ResponseEntity<String> getCustomer(@PathVariable String customerId) {
		System.out.println("*** getCustomer() called with customerId of: " + customerId + " ***");
		Customer customer = CustomerUtility.recreateCustomerState(eventStoreRepository, customerId);
		return new ResponseEntity<>("getCustomer(): " + JsonParser.toJson(customer), HttpStatus.OK);
	}

	/**
	 * getAllCustomers()
	 * 
	 * curl -i http://localhost:9082/es/customers
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value = "/customers")
	@ResponseBody
	public ResponseEntity<String> getAllCustomers() {
		System.out.println("*** getAllCustomers() called ***");
		Map<String, Customer> allCustomers = CustomerUtility.getAllCustomers(eventStoreRepository);
		return new ResponseEntity<>("getAllCustomers(): " + JsonParser.toJson(allCustomers), HttpStatus.OK);
	}

	/**
	 * getCustomerCount()
	 * 
	 * curl -i http://localhost:9082/es/customer/count
	 * 
	 * @param customerId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value = "/customer/count")
	@ResponseBody
	public ResponseEntity<String> getCustomerCount() {
		System.out.println("*** getCustomerCount() called ***");
		int count = CustomerUtility.getCustomerCount(eventStoreRepository);
		return new ResponseEntity<>("*** getCustomerCount() fetched with count of: " + count + " ***", HttpStatus.OK);
	}

	/**
	 * updateCustomer()
	 * 
	 * curl -i -H "Content-Type:application/json" -X PUT --data '{"id":"1234",
	 * "forename":"Gary", "surname":"Black", "dob":"01/01/1980", "address1":"12 Elf
	 * Avenue", "address2":"", "city":"Laplando", "postcode":"X1M2A5",
	 * "sin":"9876543210", "references":[{"forename":"Daniel", "surname":"Lester",
	 * "telephoneNumber":"416-123-4567", "sin":"8765432109"},{"forename":"Linda",
	 * "surname":"Hamilton", "telephoneNumber":"416-123-4563", "sin":"6543210978"}],
	 * "assets":[{"name":"House", "value":"$400,000"},{"name":"Vehicle",
	 * "value":"$35,000"}]}' http://localhost:9082/es/customer
	 * 
	 * @param customer
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT, consumes = "application/json", produces = "application/json", value = "/customer")
	@ResponseBody
	public ResponseEntity<String> updateCustomer(@RequestBody Customer customer) {
		System.out.println("*** updateCustomer() called ***");
		eventStoreRepository.publishEvent(new CustomerUpdatedEvent(customer), customer.getId());
		return new ResponseEntity<>("updateCustomer(): " + JsonParser.toJson(customer), HttpStatus.OK);
	}

	/**
	 * getAllReferences()
	 * 
	 * curl -i http://localhost:9082/es/customer/1234/references
	 * 
	 * @param customerId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value = "/customer/{customerId}/references")
	@ResponseBody
	public ResponseEntity<String> getAllReferences(@PathVariable String customerId) {
		System.out.println("*** getAllReferences() called with customerId of: " + customerId + " ***");
		Customer customer = CustomerUtility.recreateCustomerState(eventStoreRepository, customerId);
		Set<Reference> references = customer.getReferences();
		return new ResponseEntity<>("getAllReferences(): " + JsonParser.toJson(references), HttpStatus.OK);
	}

	/**
	 * getAllAssets()
	 * 
	 * curl -i http://localhost:9082/es/customer/1234/assets
	 * 
	 * @param customerId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value = "/customer/{customerId}/assets")
	@ResponseBody
	public ResponseEntity<String> getAllAssets(@PathVariable String customerId) {
		System.out.println("*** getAllAssets() called with customerId of: " + customerId + " ***");
		Customer customer = CustomerUtility.recreateCustomerState(eventStoreRepository, customerId);
		Set<Asset> assets = customer.getAssets();
		return new ResponseEntity<>("getAllAssets(): " + JsonParser.toJson(assets), HttpStatus.OK);
	}

	/**
	 * getReferenceBySin()
	 * 
	 * curl -i http://localhost:9082/es/customer/1234/reference/sin/5678
	 * 
	 * @param customerId
	 * @param sin
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value = "/customer/{customerId}/reference/sin/{sin}")
	@ResponseBody
	public ResponseEntity<String> getReferencesBySin(@PathVariable String customerId, @PathVariable String sin) {
		System.out.println(
				"*** getReferencesBySin() called with customerId of: " + customerId + " and sin of: " + sin + " ***");
		Customer customer = CustomerUtility.recreateCustomerState(eventStoreRepository, customerId);
		Set<Reference> references = customer.getReferences();
		Set<Reference> referencesFilteredBySin = references.stream().filter(a -> a.getSin().equals(sin))
				.collect(Collectors.toSet());
		return new ResponseEntity<>("getReferencesBySin(): " + JsonParser.toJson(referencesFilteredBySin),
				HttpStatus.OK);
	}

	/**
	 * getAssetById()
	 * 
	 * curl -i http://localhost:9082/es/customer/1234/asset/5678
	 * 
	 * @param customerId
	 * @param assetId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value = "/customer/{customerId}/asset/{assetName}")
	@ResponseBody
	public ResponseEntity<String> getAssetsByName(@PathVariable String customerId, @PathVariable String assetName) {
		System.out.println("*** getAssetsByName() called with customerId of: " + customerId + " and name of: "
				+ assetName + " ***");
		Customer customer = CustomerUtility.recreateCustomerState(eventStoreRepository, customerId);
		Set<Asset> assets = customer.getAssets();
		Set<Asset> assetsFilteredByName = assets.stream().filter(a -> a.getName().equals(assetName))
				.collect(Collectors.toSet());
		return new ResponseEntity<>("getAssetsByName(): " + JsonParser.toJson(assetsFilteredByName), HttpStatus.OK);
	}
}
