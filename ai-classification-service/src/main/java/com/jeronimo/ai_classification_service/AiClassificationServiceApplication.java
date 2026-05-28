package com.jeronimo.ai_classification_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaRetryTopic;

@EnableKafka
@EnableKafkaRetryTopic
@SpringBootApplication
public class AiClassificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiClassificationServiceApplication.class, args);
	}

}
