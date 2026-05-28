package com.jeronimo.ai_classification_service.application.usecase;

import com.jeronimo.ai_classification_service.domain.model.ReceiptClassification;
import com.jeronimo.ai_classification_service.domain.repository.ReceiptClassificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindReceiptClassificationUseCase {

    private final ReceiptClassificationRepository repository;

    public List<ReceiptClassification> findAll() {
        return repository.findAll();
    }

    public Optional<ReceiptClassification> findByReceiptId(UUID receiptId) {
        return repository.findByReceiptId(receiptId);
    }
}
