package com.jeronimo.ai_classification_service.infrastructure.kafka.config;

import com.jeronimo.ai_classification_service.domain.event.ReceiptClassifiedEvent;
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
    public ProducerFactory<String, ReceiptClassifiedEvent> receiptClassifiedProducerFactory(
            KafkaProperties kafkaProperties
    ) {
        Map<String, Object> props = kafkaProperties.buildProducerProperties();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        /*
         * Boa prática entre microserviços:
         * não enviar o nome da classe Java no header __TypeId__.
         */
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS,false);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, ReceiptClassifiedEvent> receiptClassifiedKafkaTemplate(
            ProducerFactory<String, ReceiptClassifiedEvent> producerFactory
    ) {
        KafkaTemplate<String, ReceiptClassifiedEvent> kafkaTemplate =
                new KafkaTemplate<>(producerFactory);

        /*
         * Habilita tracing Kafka producer no Jaeger.
         */
        kafkaTemplate.setObservationEnabled(true);
        return kafkaTemplate;
    }
}
