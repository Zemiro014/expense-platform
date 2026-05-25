package com.jeronimo.validation_service.infrastructure.persistence;

import com.jeronimo.validation_service.domain.model.ReceiptValidation;
import com.jeronimo.validation_service.domain.repository.ReceiptValidationRepository;
import com.jeronimo.validation_service.infrastructure.persistence.entity.ReceiptValidationEntity;
import com.jeronimo.validation_service.infrastructure.persistence.mapper.ReceiptValidationEntityMapper;
import com.jeronimo.validation_service.infrastructure.persistence.repository.SpringDataReceiptValidationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaReceiptValidationRepository implements ReceiptValidationRepository {
    private final SpringDataReceiptValidationRepository repository;

    @Override
    public ReceiptValidation save(ReceiptValidation validation) {
        ReceiptValidationEntity entity =
                ReceiptValidationEntityMapper.toEntity(validation);

        ReceiptValidationEntity savedEntity =
                repository.save(entity);

        return ReceiptValidationEntityMapper.toDomain(savedEntity);
    }

    @Override
    public List<ReceiptValidationEntity> findAll() {
        return repository.findAll();
    }
}
