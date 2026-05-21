package com.jeronimo.receipt_service.infrastructure.persistence;

import com.jeronimo.receipt_service.domain.model.Receipt;
import com.jeronimo.receipt_service.domain.repository.ReceiptRepository;
import com.jeronimo.receipt_service.infrastructure.persistence.mapper.ReceiptEntityMapper;
import com.jeronimo.receipt_service.infrastructure.persistence.repository.SpringDataReceiptRepository;
import com.jeronimo.receipt_service.presentation.response.ReceiptResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ReceiptRepositoryAdapter implements ReceiptRepository {

    private final SpringDataReceiptRepository repository;

    @Override
    public Mono<Receipt> save(Receipt receipt) {
        return repository.save(
                ReceiptEntityMapper.toEntity(receipt)
            )
            .map(ReceiptEntityMapper::toDomain);
    }

    @Override
    public Mono<Receipt> findById(UUID id) {
        return repository.findById(id)
            .map(ReceiptEntityMapper::toDomain);
    }

    @Override
    public Flux<Receipt> findAllReceipt() {
        return repository.findAll()
                .map(ReceiptEntityMapper::toDomain);
    }
}
