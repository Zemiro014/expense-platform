package com.jeronimo.receipt_service.infrastructure.messaging.kafka.config;

import com.jeronimo.receipt_service.domain.event.ReceiptCreatedEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Bean
    public ProducerFactory<String, ReceiptCreatedEvent> receiptProducerFactory(
            KafkaProperties kafkaProperties
    ) {
        Map<String, Object> props = kafkaProperties.buildProducerProperties();

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, ReceiptCreatedEvent> receiptKafkaTemplate(
            ProducerFactory<String, ReceiptCreatedEvent> producerFactory
    ) {
        KafkaTemplate<String, ReceiptCreatedEvent> kafkaTemplate =
                new KafkaTemplate<>(producerFactory);

        kafkaTemplate.setObservationEnabled(true);

        return kafkaTemplate;
    }
}
