package com.jeronimo.document_extraction_service.infrastructure.kafka.config;

import com.jeronimo.document_extraction_service.domain.event.ReceiptDocumentExtractedEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Bean
    public ProducerFactory<String, ReceiptDocumentExtractedEvent> receiptDocumentExtractedProducerFactory(
            KafkaProperties kafkaProperties
    ) {
        Map<String, Object> props = kafkaProperties.buildProducerProperties();

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, ReceiptDocumentExtractedEvent> receiptDocumentExtractedKafkaTemplate(
            ProducerFactory<String, ReceiptDocumentExtractedEvent> producerFactory
    ) {
        KafkaTemplate<String, ReceiptDocumentExtractedEvent> kafkaTemplate =
                new KafkaTemplate<>(producerFactory);

        kafkaTemplate.setObservationEnabled(true);

        return kafkaTemplate;
    }
}
