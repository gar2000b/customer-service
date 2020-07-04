package com.onlineinteract.customer.es.events;

import java.util.Date;
import java.util.UUID;

public abstract class Event {

    public final UUID id = UUID.randomUUID();
    public final Date created = new Date();
	
    public UUID getId() {
		return id;
	}
	
	public Date getCreated() {
		return created;
	}
}
