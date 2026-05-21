package com.jeronimo.receipt_service.application.usecase;

import com.jeronimo.receipt_service.application.mapper.ReceiptMapper;
import com.jeronimo.receipt_service.domain.model.Receipt;
import com.jeronimo.receipt_service.domain.repository.ReceiptRepository;
import com.jeronimo.receipt_service.infrastructure.messaging.ReceiptEventPublisher;
import com.jeronimo.receipt_service.presentation.request.CreateReceiptRequest;
import com.jeronimo.receipt_service.presentation.response.ReceiptResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CreateReceiptUseCase {

    private final ReceiptRepository receiptRepository;
    private final ReceiptEventPublisher receiptEventPublisher;

    public Mono<ReceiptResponse> execute(@Valid CreateReceiptRequest request) {
        Receipt receipt = Receipt.create(
                request.merchant(),
                request.amount()
        );
        return receiptRepository.save(receipt)
                .doOnNext(receiptEventPublisher::publishReceiptCreated)
                .map(ReceiptMapper::toResponse);
    }
}
