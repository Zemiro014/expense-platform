package com.jeronimo.notification_service.infrastructure.persistence.repository;

import com.jeronimo.notification_service.infrastructure.persistence.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataNotificationRepository extends JpaRepository<NotificationEntity, UUID> {
    boolean existsByReceiptId(UUID receiptId);
}
