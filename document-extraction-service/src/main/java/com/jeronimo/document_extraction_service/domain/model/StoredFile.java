package com.jeronimo.document_extraction_service.domain.model;

import com.jeronimo.document_extraction_service.domain.event.StoredFileEvent;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Builder
public class StoredFile {

    private static final Map<FileStatus, Set<FileStatus>> ALLOWED_TRANSITIONS =
        Map.of(
            FileStatus.UPLOADED, Set.of(FileStatus.STORED),
            FileStatus.STORED, Set.of(FileStatus.OCR_PROCESSING),
            FileStatus.OCR_PROCESSING, Set.of(FileStatus.TEXT_EXTRACTED, FileStatus.OCR_FAILED),
            FileStatus.TEXT_EXTRACTED, Set.of(),
            FileStatus.OCR_FAILED, Set.of()
        );

    private UUID id;
    private String originalFilename;
    private String storedFilename;
    private String contentType;
    private Long size;
    private String storagePath;
    private FileStatus status;
    private String extractedText;
    private String errorMessage;
    @Builder.Default
    private List<StoredFileEvent> documentExtractionHistoryProcess = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static StoredFile create(
            String originalFilename,
            String storedFilename,
            String contentType,
            Long size,
            String storagePath
    ) {
        LocalDateTime now = LocalDateTime.now();

        StoredFile file = StoredFile.builder()
            .id(UUID.randomUUID())
            .originalFilename(originalFilename)
            .storedFilename(storedFilename)
            .contentType(contentType)
            .size(size)
            .storagePath(storagePath)
            .status(FileStatus.UPLOADED)
            .createdAt(now)
            .updatedAt(now)
            .documentExtractionHistoryProcess(new ArrayList<>())
            .build();

        file.documentExtractionHistoryProcess.add(
            StoredFileEvent.create(
                FileStatus.UPLOADED,
                Map.of(
                    "originalFilename", originalFilename,
                    "contentType", contentType,
                    "size", size
                )
            )
        );

        return file;
    }

    public void registerStored(Map<String, Object> payload) {
        transitionTo(FileStatus.STORED, payload);
    }

    public void registerOcrProcessing(Map<String, Object> payload) {
        transitionTo(FileStatus.OCR_PROCESSING, payload);
    }

    public void registerTextExtracted(String extractedText, Map<String, Object> payload) {
        this.extractedText = extractedText;
        this.errorMessage = null;

        transitionTo(FileStatus.TEXT_EXTRACTED, payload);
    }

    public void registerOcrFailed(Map<String, Object> payload) {
        Object errorMessageValue = payload.get("errorMessage");

        this.errorMessage = errorMessageValue == null ? null : errorMessageValue.toString();

        transitionTo(FileStatus.OCR_FAILED, payload);
    }

    private void transitionTo(FileStatus nextStatus, Map<String, Object> payload) {
        validateTransition(this.status, nextStatus);

        this.status = nextStatus;
        this.updatedAt = LocalDateTime.now();

        this.documentExtractionHistoryProcess.add(StoredFileEvent.create(nextStatus, payload == null ? Map.of() : payload));
    }

    private void validateTransition(FileStatus currentStatus, FileStatus nextStatus) {
        Set<FileStatus> allowedNextStatuses = ALLOWED_TRANSITIONS.getOrDefault(currentStatus, Set.of());

        if (!allowedNextStatuses.contains(nextStatus)) {
            throw new IllegalStateException(
                "Invalid file status transition: "
                        + currentStatus
                        + " -> "
                        + nextStatus
            );
        }
    }
}
