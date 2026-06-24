package com.jeronimo.document_extraction_service.presentation.controller;

import com.jeronimo.document_extraction_service.application.usecase.FindStoredFileUseCase;
import com.jeronimo.document_extraction_service.application.usecase.UploadReceiptFileUseCase;
import com.jeronimo.document_extraction_service.domain.model.StoredFile;
import com.jeronimo.document_extraction_service.presentation.mapper.StoredFileResponseMapper;
import com.jeronimo.document_extraction_service.presentation.response.StoredFileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StoredFileResponse> upload(
            @RequestParam("file") MultipartFile file,
        @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId
    ) {
        try {
            MDC.put("event", "receipt_file_upload_requested");
            StoredFile storedFile = uploadUseCase.execute(file);

            return ResponseEntity.ok(StoredFileResponseMapper.toResponse(storedFile));
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
