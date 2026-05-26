package com.jeronimo.notification_service.infrastructure.persistence.entity;

import com.jeronimo.notification_service.domain.model.NotificationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "notifications")
public class NotificationEntity {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID receiptId;

    @Column(nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    private LocalDateTime createdAt;
}
