package com.jeronimo.document_extraction_service.domain.event;

import com.jeronimo.document_extraction_service.domain.model.FileStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
public class StoredFileEvent {
    private FileStatus status;
    private LocalDateTime occurredAt;
    private Map<String, Object> payload;

    public static StoredFileEvent create(
            FileStatus status,
            Map<String, Object> payload)
    {
        return StoredFileEvent.builder()
                .status(status)
                .payload(payload)
                .occurredAt(LocalDateTime.now())
                .build();
    }
}
