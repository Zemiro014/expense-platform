package com.jeronimo.receipt_service.domain.repository;

import com.jeronimo.receipt_service.domain.model.Receipt;
import com.jeronimo.receipt_service.presentation.response.ReceiptResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReceiptRepository {
    Mono<Receipt> save(Receipt receipt);

    Mono<Receipt> findById(UUID id);

    Flux<Receipt> findAllReceipt();
}
