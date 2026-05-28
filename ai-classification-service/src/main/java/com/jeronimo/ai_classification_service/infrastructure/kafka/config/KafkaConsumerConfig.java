package com.jeronimo.ai_classification_service.infrastructure.kafka.config;

import com.jeronimo.ai_classification_service.domain.event.ReceiptCreatedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, ReceiptCreatedEvent> receiptCreatedConsumerFactor(
            KafkaProperties kafkaProperties
    ) {
        Map<String, Object> props = kafkaProperties.buildConsumerProperties();
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        JsonDeserializer<ReceiptCreatedEvent> deserializer = new JsonDeserializer<>(ReceiptCreatedEvent.class);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeHeaders(false); // Ignore the Type Header
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ReceiptCreatedEvent>
    kafkaListenerContainerFactory(
            ConsumerFactory<String, ReceiptCreatedEvent> consumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, ReceiptCreatedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);
        factory.setMissingTopicsFatal(false);
        factory.setCommonErrorHandler(new DefaultErrorHandler());

        /*
         * Enable spans Kafka io Jaeger.
         */
        factory.getContainerProperties().setObservationEnabled(true);

        return factory;
    }
}
