package com.jeronimo.validation_service.domain.event.outbox;

public enum OutboxStatus {
    PENDING,
    PUBLISHED,
    FAILED
}
