package com.jeronimo.document_extraction_service.presentation.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.jeronimo.document_extraction_service.domain.model.FileStatus;

import java.time.LocalDateTime;
import java.util.List;
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
        LocalDateTime updatedAt,
        JsonNode documentExtractionEventsProcess
) { }
