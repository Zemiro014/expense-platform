package com.jeronimo.document_extraction_service.presentation.response;

import com.jeronimo.document_extraction_service.domain.model.FileStatus;

import java.time.LocalDateTime;
import java.util.Map;

public record StoredFileEventResponse(
        FileStatus status,
        LocalDateTime occurredAt,
        Map<String, Object> payload
) { }
