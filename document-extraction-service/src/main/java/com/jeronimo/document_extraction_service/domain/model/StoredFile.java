package com.jeronimo.document_extraction_service.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class StoredFile {

    private UUID id;
    private String originalFilename;
    private String storedFilename;
    private String contentType;
    private Long size;
    private String storagePath;
    private FileStatus status;
    private String extractedText;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static StoredFile uploaded(
        String originalFilename,
        String storedFilename,
        String contentType,
        Long size,
        String storagePath
    ) {
        LocalDateTime now = LocalDateTime.now();

        return StoredFile.builder()
            .id(UUID.randomUUID())
            .originalFilename(originalFilename)
            .storedFilename(storedFilename)
            .contentType(contentType)
            .size(size)
            .storagePath(storagePath)
            .status(FileStatus.UPLOADED)
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    public StoredFile markStored() {
        this.status = FileStatus.STORED;
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public StoredFile markOcrProcessing() {
        this.status = FileStatus.OCR_PROCESSING;
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public StoredFile markTextExtracted(String extractedText) {
        this.status = FileStatus.TEXT_EXTRACTED;
        this.extractedText = extractedText;
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public StoredFile markOcrFailed(String errorMessage) {
        this.status = FileStatus.OCR_FAILED;
        this.errorMessage = errorMessage;
        this.updatedAt = LocalDateTime.now();
        return this;
    }
}
