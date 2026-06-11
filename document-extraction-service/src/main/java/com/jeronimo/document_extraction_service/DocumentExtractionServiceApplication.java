package com.jeronimo.document_extraction_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class DocumentExtractionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocumentExtractionServiceApplication.class, args);
	}

}
