package com.jeronimo.notification_service.application.usecase;

import com.jeronimo.notification_service.domain.event.ReceiptValidatedEvent;
import com.jeronimo.notification_service.domain.model.Notification;
import com.jeronimo.notification_service.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateNotificationUseCase {
    private final NotificationRepository repository;

    public void execute(ReceiptValidatedEvent event) {
        if (repository.existsByReceiptId(event.receiptId())) {
            MDC.put("event", "notification_skipped");
            log.info("Notification already exists. Skipping duplicated event");
            return;
        }
        String message = buildMessage(event);

        Notification notification =
                Notification.create(event.receiptId(), message );

        repository.save(notification);
        MDC.put("event","notification_created");
        log.info("Notification created successfully");
    }
    private String buildMessage(ReceiptValidatedEvent event) {
        return String.format(
                "Receipt %s was validated with status %s",
                event.receiptId(),
                event.status()
        );
    }
}
