package com.jeronimo.receipt_service.application.usecase;

import com.jeronimo.receipt_service.application.mapper.ReceiptEventMapper;
import com.jeronimo.receipt_service.application.mapper.ReceiptMapper;
import com.jeronimo.receipt_service.domain.model.Receipt;
import com.jeronimo.receipt_service.domain.repository.ReceiptRepository;
import com.jeronimo.receipt_service.domain.publisher.ReceiptEventPublisher;
import com.jeronimo.receipt_service.presentation.request.CreateReceiptRequest;
import com.jeronimo.receipt_service.presentation.response.ReceiptResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
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
                .doOnNext(savedReceipt -> {
                    MDC.put("receiptId", savedReceipt.getId().toString());
                    MDC.put("event", "receipt_created");
                    log.info("Receipt created successfully");
                })
                .flatMap(savedReceipt ->
                    receiptEventPublisher.publishReceiptCreated(ReceiptEventMapper.toCreatedEvent(savedReceipt))
                        .thenReturn(savedReceipt)
                ).map(ReceiptMapper::toResponse);
    }
}
