package com.jeronimo.receipt_service.application.mapper;

import com.jeronimo.receipt_service.domain.model.Receipt;
import com.jeronimo.receipt_service.presentation.response.ReceiptResponse;

public class ReceiptMapper {
    private ReceiptMapper() {
    }

    public static ReceiptResponse toResponse(
            Receipt receipt) {
        return new ReceiptResponse(
                receipt.getId(),
                receipt.getMerchant(),
                receipt.getAmount(),
                receipt.getStatus().name()
        );
    }
}
