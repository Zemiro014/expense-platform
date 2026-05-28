package com.jeronimo.ai_classification_service.infrastructure.ia.ollama.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class OllamaWebClientConfig {
    @Bean
    public RestClient ollamaRestClient(
            RestClient.Builder builder,
            @Value("${app.ai.ollama.base-url}") String baseUrl
    ) {
        return builder
                .baseUrl(baseUrl)
                .build();
    }
}
