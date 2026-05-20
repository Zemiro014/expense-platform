package com.jeronimo.receipt_service;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class ReceiptServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReceiptServiceApplication.class, args);
	}

	@Bean
    CommandLineRunner testarConexaoBanco(ConnectionFactory connectionFactory) {
		return args -> {
			Mono.from(connectionFactory.create())
					.flatMap(connection -> {
						System.out.println("✅ CONEXÃO COM O POSTGRES REALIZADA COM SUCESSO!");
						return Mono.from(connection.close());
					})
					.doOnError(error -> System.err.println("❌ FALHA AO CONECTAR NO BANCO: " + error.getMessage()))
					.subscribe();
		};
	}
}
