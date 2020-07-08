package com.onlineinteract.fileserver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import com.onlineinteract.customer.domain.Customer;
import com.onlineinteract.customer.es.events.CustomerCreatedEvent;
import com.onlineinteract.customer.utility.JsonParser;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class CustomerTests {

	private KafkaProducer<String, String> producer;
	private KafkaConsumer<String, String> consumer;

	@Before
	public void setup() {
		final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		logger.setLevel(Level.INFO);
	}

	@Test
	@Ignore
	public void publish1000000CustomerRecords() throws InterruptedException {
		createProducer();
		publishCustomerRecords(1000000);
		Thread.sleep(20000);
	}

	@Test
	@Ignore
	public void consume1000000CustomerRecords() {
		createConsumer();
		consumeCustomerRecords(1000000);
	}

	private void consumeCustomerRecords(int noOfRecords) {
		consumer.poll(0);
		consumer.seekToBeginning(consumer.assignment());
		System.out.println("Spinning up kafka consumer");

		int total = 0;
		long startTime = System.currentTimeMillis();
		long endTime;
		long totalTime;
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(10000);
			total += records.count();
			endTime = System.currentTimeMillis();
			totalTime = endTime - startTime;
			System.out.println("Count is: " + total + " - time taken: " + totalTime + " ms");
			for (ConsumerRecord<String, String> consumerRecord : records) {
				// Fetch record
				consumerRecord.value();
			}
			if (total >= noOfRecords) {
				break;
			}
		}
		shutdownConsumerProducer();
		System.out.println("Shutting down kafka consumer");
	}

	private void publishCustomerRecords(int noOfRecords) {
		System.out.println("Processing " + noOfRecords + " records");
		List<String> customerCreatedJsonEvents = new ArrayList<>();
		for (int i = 0; i < noOfRecords; i++) {
			String customerId = UUID.randomUUID().toString();
			Customer customer = JsonParser.fromJson(
					"{\"id\":\"\",   \"forename\":\"Jennifer\", \"surname\":\"Hagen\", \"dob\":\"01/01/1980\", \"address1\":\"12 Elf   Avenue\", \"address2\":\"\", \"city\":\"Laplando\", \"postcode\":\"X1M2A5\",   \"sin\":\"9876543210\", \"references\":[{\"forename\":\"Daniel\", \"surname\":\"Lester\",   \"telephoneNumber\":\"416-123-4567\", \"sin\":\"8765432109\"},{\"forename\":\"Linda\",   \"surname\":\"Hamilton\", \"telephoneNumber\":\"416-123-4563\", \"sin\":\"6543210978\"}],   \"assets\":[{\"name\":\"House\", \"value\":\"$400,000\"},{\"name\":\"Vehicle\",   \"value\":\"$35,000\"}]}",
					Customer.class);
			customer.setId(customerId);
			CustomerCreatedEvent customerCreatedEvent = new CustomerCreatedEvent(customer);
			customerCreatedEvent.setId(String.valueOf(customerCreatedEvent.getCreated().getTime()));
			customerCreatedJsonEvents.add(JsonParser.toJson(customerCreatedEvent));
		}

		for (String customerCreatedJsonEvent : customerCreatedJsonEvents) {
			producer.send(new ProducerRecord<>("customer-event-topic", UUID.randomUUID().toString(),
					customerCreatedJsonEvent));
//			System.out.println("Record published");
		}
		System.out.println("All records published");
	}

	private void createProducer() {
		Properties producerProps = buildProducerProperties();
		producer = new KafkaProducer<String, String>(producerProps, new StringSerializer(), new StringSerializer());
		System.out.println("Kafka Producer instantiated");
	}

	private Properties buildProducerProperties() {
		Properties properties = new Properties();
		properties.put("bootstrap.servers", "localhost:9092");
		properties.put("key.serializer", StringSerializer.class);
		properties.put("value.serializer", StringSerializer.class);
		return properties;
	}

	private void createConsumer() {
		Properties buildProperties = buildConsumerProperties();
		consumer = new KafkaConsumer<>(buildProperties);
		consumer.subscribe(Arrays.asList("customer-event-topic"));
	}

	public void shutdownConsumerProducer() {
		System.out.println("*** consumer shutting down");
		consumer.close();
	}

	private Properties buildConsumerProperties() {
		Properties properties = new Properties();
//		properties.put("bootstrap.servers", "tiny.canadacentral.cloudapp.azure.com:29092");
		properties.put("bootstrap.servers", "localhost:9092");
		properties.put("group.id", "customer-event-topic-group1");
		properties.put("enable.auto.commit", "false");
		properties.put("max.poll.records", "2000");
		properties.put("key.deserializer", StringDeserializer.class);
		properties.put("value.deserializer", StringDeserializer.class);
		return properties;
	}

}
