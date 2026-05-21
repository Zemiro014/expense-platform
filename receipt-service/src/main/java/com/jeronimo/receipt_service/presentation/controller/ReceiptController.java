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
    public Mono<ResponseEntity<ReceiptResponse>> create(@Valid @RequestBody CreateReceiptRequest request){
        return createReceiptUseCase.execute(request)
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
    public Mono<ResponseEntity<ReceiptResponse>> findById(@PathVariable UUID id) {
        return findReceiptByIdUseCase.execute(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
