package com.jeronimo.ai_classification_service.presentation.controller;

import com.jeronimo.ai_classification_service.application.usecase.FindAiInferenceAuditUseCase;
import com.jeronimo.ai_classification_service.application.usecase.GetAiInferenceSummaryUseCase;
import com.jeronimo.ai_classification_service.presentation.mapper.AiInferenceAuditResponseMapper;
import com.jeronimo.ai_classification_service.presentation.response.AiInferenceAuditResponse;
import com.jeronimo.ai_classification_service.presentation.response.AiInferenceSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ai-audits")
@RequiredArgsConstructor
public class AiInferenceAuditController {

    private final FindAiInferenceAuditUseCase findUseCase;
    private final GetAiInferenceSummaryUseCase summaryUseCase;

    @GetMapping
    public ResponseEntity<List<AiInferenceAuditResponse>> findAll() {

        try {
            MDC.put("event", "ai_audit_find_all");

            List<AiInferenceAuditResponse> response =
                findUseCase.findAll()
                    .stream()
                    .map(AiInferenceAuditResponseMapper::toResponse)
                    .toList();

            return ResponseEntity.ok(response);

        } finally {
            MDC.remove("event");
        }
    }

    @GetMapping("/{receiptId}")
    public ResponseEntity<List<AiInferenceAuditResponse>> findByReceiptId(
            @PathVariable UUID receiptId
    ) {
        try {
            MDC.put("event", "ai_audit_find_by_receipt");
            MDC.put("receiptId", receiptId.toString());

            List<AiInferenceAuditResponse> response =
                findUseCase.findByReceiptId(receiptId)
                    .stream()
                    .map(AiInferenceAuditResponseMapper::toResponse)
                    .toList();

            return ResponseEntity.ok(response);

        } finally {
            MDC.remove("event");
            MDC.remove("receiptId");
        }
    }

    @GetMapping("/summary")
    public ResponseEntity<AiInferenceSummaryResponse> summary() {
        try {
            MDC.put("event", "ai_metrics_summary_requested");

            return ResponseEntity.ok(summaryUseCase.execute());

        } finally {
            MDC.remove("event");
        }
    }
}
