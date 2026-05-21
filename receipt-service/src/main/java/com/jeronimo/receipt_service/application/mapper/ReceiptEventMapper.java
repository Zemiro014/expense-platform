package com.jeronimo.receipt_service.application.mapper;

import com.jeronimo.receipt_service.domain.event.ReceiptCreatedEvent;
import com.jeronimo.receipt_service.domain.model.Receipt;

public class ReceiptEventMapper {
    private ReceiptEventMapper() {
    }

    public static ReceiptCreatedEvent toCreatedEvent(Receipt receipt) {
        return new ReceiptCreatedEvent(
                receipt.getId(),
                receipt.getMerchant(),
                receipt.getAmount(),
                receipt.getStatus().name(),
                receipt.getCreatedAt()
        );
    }
}
