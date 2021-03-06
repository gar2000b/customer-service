package com.onlineinteract.customer.es.events;

import com.onlineinteract.customer.domain.Customer;

public class CustomerCreatedEvent extends Event {
	private Customer customer;

	public CustomerCreatedEvent() {
	}

	public CustomerCreatedEvent(Customer customer) {
		this.customer = customer;
		this.setType("CustomerCreatedEvent");
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((customer == null) ? 0 : customer.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerCreatedEvent other = (CustomerCreatedEvent) obj;
		if (customer == null) {
			if (other.customer != null)
				return false;
		} else if (!customer.equals(other.customer))
			return false;
		return true;
	}
}
