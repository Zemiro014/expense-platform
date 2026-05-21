package com.jeronimo.receipt_service.presentation.request;

import java.math.BigDecimal;

public record CreateReceiptRequest (
        String merchant,
        BigDecimal amount
){}
