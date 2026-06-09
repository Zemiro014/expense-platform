package com.jeronimo.validation_service.infrastructure.scheduler;

import com.jeronimo.validation_service.domain.event.outbox.OutboxEvent;
import com.jeronimo.validation_service.domain.repository.outbox.OutboxEventRepository;
import com.jeronimo.validation_service.infrastructure.kafka.publisher.outbox.OutboxEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisherScheduler {
    private final OutboxEventRepository outboxRepository;
    private final OutboxEventPublisher publisher;

    @Scheduled(fixedDelayString = "${app.outbox.publisher.fixed-delay-ms:5000}")
    public void publishPendingEvents() {
        try {
            MDC.put("event", "outbox_publisher_started");

            List<OutboxEvent> events =
                    outboxRepository.findPending(10);

            MDC.put("pendingEvents", String.valueOf(events.size()));

            if (events.isEmpty()) {
                return;
            }

            log.info("Publishing pending outbox events");

            events.forEach(publisher::publish);

            MDC.put("event", "outbox_publisher_finished");
            log.info("Outbox publisher finished");

        } finally {
            MDC.clear();
        }
    }
}
