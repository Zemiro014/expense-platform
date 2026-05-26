package com.jeronimo.notification_service.infrastructure.kafka.config;

import com.jeronimo.notification_service.domain.event.ReceiptValidatedEvent;
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
    public ConsumerFactory<String, ReceiptValidatedEvent> receiptValidatedConsumerFactory(
            KafkaProperties kafkaProperties
    ) {
        Map<String, Object> props = kafkaProperties.buildProducerProperties();
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        JsonDeserializer<ReceiptValidatedEvent> deserializer = new JsonDeserializer<>(ReceiptValidatedEvent.class);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeHeaders(false);
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ReceiptValidatedEvent> kafkaListenerContainerFactory(
            ConsumerFactory<String, ReceiptValidatedEvent> consumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, ReceiptValidatedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(new DefaultErrorHandler());
        factory.setMissingTopicsFatal(false);
        factory.getContainerProperties().setObservationEnabled(true);

        return factory;
    }
}
