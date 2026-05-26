package com.jeronimo.notification_service.domain.repository;

import com.jeronimo.notification_service.domain.model.Notification;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository {
    Notification save(Notification notification);

    boolean existsByReceiptId(UUID receiptId);

    List<Notification> findAll();
}
