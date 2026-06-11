package com.jeronimo.document_extraction_service.presentation.controller;

import com.jeronimo.document_extraction_service.application.usecase.FindStoredFileUseCase;
import com.jeronimo.document_extraction_service.application.usecase.UploadReceiptFileUseCase;
import com.jeronimo.document_extraction_service.presentation.mapper.StoredFileResponseMapper;
import com.jeronimo.document_extraction_service.presentation.response.StoredFileResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/files/receipts")
@RequiredArgsConstructor
public class ReceiptFileController {

    private final UploadReceiptFileUseCase uploadUseCase;
    private final FindStoredFileUseCase findUseCase;

    @PostMapping
    public ResponseEntity<StoredFileResponse> upload(
        @RequestPart("file") MultipartFile file,
        @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId
    ) {
        try {
            String safeCorrelationId =
                correlationId == null || correlationId.isBlank()
                    ? UUID.randomUUID().toString()
                    : correlationId;

            MDC.put("correlationId", safeCorrelationId);
            MDC.put("event", "receipt_file_upload_requested");

            return ResponseEntity.ok(
                StoredFileResponseMapper.toResponse(
                        uploadUseCase.execute(file, safeCorrelationId)
                )
            );

        } finally {
            MDC.clear();
        }
    }

    @GetMapping
    public ResponseEntity<List<StoredFileResponse>> findAll() {
        return ResponseEntity.ok(
            findUseCase.findAll()
                .stream()
                .map(StoredFileResponseMapper::toResponse)
                .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoredFileResponse> findById(
            @PathVariable UUID id
    ) {
        return findUseCase.findById(id)
            .map(StoredFileResponseMapper::toResponse)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
