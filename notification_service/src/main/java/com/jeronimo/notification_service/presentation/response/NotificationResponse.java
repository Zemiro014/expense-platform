package com.jeronimo.notification_service.presentation.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationResponse(
        UUID id,
        UUID receiptId,
        String message,
        String status,
        LocalDateTime createdAt
) { }
