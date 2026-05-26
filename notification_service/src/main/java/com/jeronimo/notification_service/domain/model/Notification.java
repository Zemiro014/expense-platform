package com.jeronimo.notification_service.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class Notification {

    private UUID id;
    private UUID receiptId;
    private String message;
    private NotificationStatus status;
    private LocalDateTime createdAt;

    public static Notification create(
            UUID receiptId,
            String message
    ) {
        return Notification.builder()
            .id(UUID.randomUUID())
            .receiptId(receiptId)
            .message(message)
            .status(NotificationStatus.PENDING)
            .createdAt(LocalDateTime.now())
            .build();
    }
}
