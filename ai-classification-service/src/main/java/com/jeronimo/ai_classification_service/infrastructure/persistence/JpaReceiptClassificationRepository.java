package com.jeronimo.ai_classification_service.infrastructure.persistence;

import com.jeronimo.ai_classification_service.domain.model.ReceiptClassification;
import com.jeronimo.ai_classification_service.domain.repository.ReceiptClassificationRepository;
import com.jeronimo.ai_classification_service.infrastructure.persistence.entity.ReceiptClassificationEntity;
import com.jeronimo.ai_classification_service.infrastructure.persistence.mapper.ReceiptClassificationEntityMapper;
import com.jeronimo.ai_classification_service.infrastructure.persistence.repository.SpringDataReceiptClassificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaReceiptClassificationRepository implements ReceiptClassificationRepository {

    private final SpringDataReceiptClassificationRepository repository;

    @Override
    public ReceiptClassification save(ReceiptClassification classification) {
        ReceiptClassificationEntity entity =
                ReceiptClassificationEntityMapper.toEntity(classification);

        ReceiptClassificationEntity savedEntity =
                repository.save(entity);

        return ReceiptClassificationEntityMapper.toDomain(savedEntity);
    }

    @Override
    public boolean existsByReceiptId(UUID receiptId) {
        return repository.existsByReceiptId(receiptId);
    }

    @Override
    public List<ReceiptClassification> findAll() {
        return repository.findAll()
                .stream()
                .map(ReceiptClassificationEntityMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<ReceiptClassification> findByReceiptId(UUID receiptId) {
        return repository.findByReceiptId(receiptId)
                .map(ReceiptClassificationEntityMapper::toDomain);
    }
}
