package com.jeronimo.notification_service.infrastructure.persistence.mapper;

import com.jeronimo.notification_service.domain.model.Notification;
import com.jeronimo.notification_service.infrastructure.persistence.entity.NotificationEntity;

public class NotificationEntityMapper {
    private NotificationEntityMapper() {
    }

    public static NotificationEntity toEntity(
            Notification notification
    ) {
        NotificationEntity entity =
                new NotificationEntity();

        entity.setId(notification.getId());
        entity.setReceiptId(notification.getReceiptId());
        entity.setMessage(notification.getMessage());
        entity.setStatus(notification.getStatus());
        entity.setCreatedAt(notification.getCreatedAt());

        return entity;
    }

    public static Notification toDomain(
            NotificationEntity entity
    ) {
        return Notification.builder()
                .id(entity.getId())
                .receiptId(entity.getReceiptId())
                .message(entity.getMessage())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
