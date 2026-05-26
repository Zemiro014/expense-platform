package com.jeronimo.notification_service.application.mapper;

import com.jeronimo.notification_service.domain.model.Notification;
import com.jeronimo.notification_service.presentation.response.NotificationResponse;

public class NotificationMapper {
    private NotificationMapper(){}

    public static NotificationResponse toResponse(Notification notification){
        return new NotificationResponse(
                notification.getId(),
                notification.getReceiptId(),
                notification.getMessage(),
                notification.getStatus().toString(),
                notification.getCreatedAt()
        );
    }
}
