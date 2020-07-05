package com.onlineinteract.customer.es.bus;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;

@Component
public class Producer {
	private KafkaProducer<String, String> producer;

	public Producer() {
		Properties producerProps = buildProducerProperties();
		producer = new KafkaProducer<String, String>(producerProps, new StringSerializer(), new StringSerializer());
	}

	public void publishRecord(String topic, String value, String key) {
		producer.send(new ProducerRecord<>(topic, key, value));
	}

	private Properties buildProducerProperties() {
		Properties properties = new Properties();
		properties.put("bootstrap.servers", "tiny.canadacentral.cloudapp.azure.com:29092");
		properties.put("key.serializer", StringSerializer.class);
		properties.put("value.serializer", StringSerializer.class);
		return properties;
	}
}
