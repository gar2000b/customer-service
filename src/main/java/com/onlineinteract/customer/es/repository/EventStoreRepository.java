package com.onlineinteract.customer.es.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.onlineinteract.customer.es.bus.Producer;
import com.onlineinteract.customer.es.events.CustomerCreatedEvent;
import com.onlineinteract.customer.es.events.CustomerUpdatedEvent;
import com.onlineinteract.customer.es.events.Event;
import com.onlineinteract.customer.utility.JsonParser;

@Repository
public class EventStoreRepository {
	
	@Autowired
	private Producer producer;
	
    private Map<String, List<Event>> store = new HashMap<>();

    public void addEvent(Event event) {
    	String id = null;
    	if (event instanceof CustomerCreatedEvent)
    		id = ((CustomerCreatedEvent) event).getCustomer().getId();
    	if (event instanceof CustomerUpdatedEvent)
    		id = ((CustomerUpdatedEvent) event).getCustomer().getId();
    	
        List<Event> events = store.get(id);
        if (events == null) {
            events = new ArrayList<Event>();
            events.add(event);
            store.put(id, events);
        } else {
            events.add(event);
        }
    }

    public List<Event> getEvents(String id) {
        return store.get(id);
    }
    
    public Set<String> getEventIds() {
    	return store.keySet();
    }
    
    public int getEventCount() {
    	return store.size();
    }
    
    public void publishEvent(Event event, String recordKey) {
    	producer.publishRecord("customer-event-topic", JsonParser.toJson(event), recordKey);
    	System.out.println("**** customer event published to customer-event-topic ****");
    }
}
