package com.jeronimo.receipt_service.presentation.controller;

import com.jeronimo.receipt_service.application.usecase.CreateReceiptUseCase;
import com.jeronimo.receipt_service.application.usecase.FindAllReceiptUseCase;
import com.jeronimo.receipt_service.application.usecase.FindReceiptByIdUseCase;
import com.jeronimo.receipt_service.presentation.request.CreateReceiptRequest;
import com.jeronimo.receipt_service.presentation.response.ReceiptResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final CreateReceiptUseCase createReceiptUseCase;
    private final FindAllReceiptUseCase findAllReceiptUseCase;
    private final FindReceiptByIdUseCase findReceiptByIdUseCase;

    @PostMapping
    public Mono<ResponseEntity<ReceiptResponse>> create(
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId,
            @Valid @RequestBody CreateReceiptRequest request
    ){
        String finalCorrelationId = correlationId == null || correlationId.isBlank()
                ? UUID.randomUUID().toString()
                : correlationId;

        return createReceiptUseCase.execute(request, finalCorrelationId)
            .map(response ->
                ResponseEntity.status(HttpStatus.CREATED)
                    .body(response)
            );
    }

    @GetMapping
    public Flux<ReceiptResponse> findAll() {
        return findAllReceiptUseCase.execute();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ReceiptResponse>> findById(
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId,
            @PathVariable UUID id) {
        String finalCorrelationId = correlationId == null || correlationId.isBlank()
                ? UUID.randomUUID().toString()
                : correlationId;
        return findReceiptByIdUseCase.execute(id, finalCorrelationId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
