package com.onlineinteract.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Configuration
public class CustomerServiceApp {

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApp.class, args);
	}
}
