package com.jeronimo.receipt_service.application.usecase;

import com.jeronimo.receipt_service.application.mapper.ReceiptMapper;
import com.jeronimo.receipt_service.domain.repository.ReceiptRepository;
import com.jeronimo.receipt_service.presentation.response.ReceiptResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class FindAllReceiptUseCase {
    private final ReceiptRepository receiptRepository;
    public Flux<ReceiptResponse> execute(){
        return receiptRepository.findAllReceipt()
                .map(ReceiptMapper::toResponse);
    }
}
