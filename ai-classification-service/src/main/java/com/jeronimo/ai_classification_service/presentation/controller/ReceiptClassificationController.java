package com.jeronimo.ai_classification_service.presentation.controller;

import com.jeronimo.ai_classification_service.application.usecase.FindReceiptClassificationUseCase;
import com.jeronimo.ai_classification_service.presentation.mapper.ReceiptClassificationResponseMapper;
import com.jeronimo.ai_classification_service.presentation.response.ReceiptClassificationResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/classifications")
@RequiredArgsConstructor
public class ReceiptClassificationController {

    private final FindReceiptClassificationUseCase findUseCase;

    @GetMapping
    public ResponseEntity<List<ReceiptClassificationResponse>> findAll() {
        try {
            MDC.put("event", "receipt_classifications_find_all");

            List<ReceiptClassificationResponse> response =
                    findUseCase.findAll()
                            .stream()
                            .map(ReceiptClassificationResponseMapper::toResponse)
                            .toList();

            return ResponseEntity.ok(response);

        } finally {
            MDC.remove("event");
        }
    }

    @GetMapping("/{receiptId}")
    public ResponseEntity<ReceiptClassificationResponse> findByReceiptId(
            @PathVariable UUID receiptId
    ) {
        try {
            MDC.put("event", "receipt_classification_find_by_receipt_id");
            MDC.put("receiptId", receiptId.toString());

            return findUseCase.findByReceiptId(receiptId)
                    .map(ReceiptClassificationResponseMapper::toResponse)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());

        } finally {
            MDC.remove("event");
            MDC.remove("receiptId");
        }
    }
}
