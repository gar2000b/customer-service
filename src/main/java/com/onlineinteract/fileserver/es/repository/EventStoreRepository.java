package com.onlineinteract.fileserver.es.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.onlineinteract.fileserver.es.events.Event;

@Repository
public class EventStoreRepository {
    private Map<String, List<Event>> store = new HashMap<>();

    public void addEvent(String id, Event event) {
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
}
