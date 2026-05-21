package com.jeronimo.receipt_service.application.usecase;

import com.jeronimo.receipt_service.application.mapper.ReceiptMapper;
import com.jeronimo.receipt_service.domain.repository.ReceiptRepository;
import com.jeronimo.receipt_service.presentation.response.ReceiptResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FindReceiptByIdUseCase {
    private final ReceiptRepository receiptRepository;

    public Mono<ReceiptResponse> execute(UUID id){
        return receiptRepository.findById(id)
                .map(ReceiptMapper::toResponse);
    }
}
