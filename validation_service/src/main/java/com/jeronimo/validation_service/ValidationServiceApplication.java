package com.jeronimo.validation_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaRetryTopic;

@SpringBootApplication
@EnableKafka
@EnableKafkaRetryTopic
public class ValidationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ValidationServiceApplication.class, args);
	}

}
