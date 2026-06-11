package com.jeronimo.document_extraction_service.presentation.response;

import com.jeronimo.document_extraction_service.domain.model.FileStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record StoredFileResponse(
        UUID id,
        String originalFilename,
        String contentType,
        Long size,
        FileStatus status,
        String extractedText,
        String errorMessage,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) { }
