package com.jeronimo.document_extraction_service.application.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeronimo.document_extraction_service.domain.event.OutboxEvent;
import com.jeronimo.document_extraction_service.domain.event.ReceiptDocumentExtractedEvent;
import com.jeronimo.document_extraction_service.domain.model.StoredFile;
import com.jeronimo.document_extraction_service.domain.publisher.ReceiptDocumentExtractedEventPublisher;
import com.jeronimo.document_extraction_service.domain.repository.OutboxEventRepository;
import com.jeronimo.document_extraction_service.domain.repository.StoredFileRepository;
import com.jeronimo.document_extraction_service.domain.service.FileStorageService;
import com.jeronimo.document_extraction_service.domain.service.OcrService;
import com.jeronimo.document_extraction_service.domain.service.StoredFileLocation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadReceiptFileUseCase {

    private static final int EVENT_VERSION = 1;
    private static final long MAX_FILE_SIZE_BYTES = 25 * 1024 * 1024;

    private static final String AGGREGATE_TYPE = "RECEIPT_DOCUMENT";
    private static final String EVENT_TYPE = "RECEIPT_DOCUMENT_EXTRACTED";

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/png",
            "image/jpeg",
            "application/pdf"
    );

    private final FileStorageService fileStorageService;
    private final OcrService ocrService;
    private final StoredFileRepository storedFileRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @Value("${app.kafka.topics.receipt-document-extracted}")
    private String receiptDocumentExtractedTopic;

    @Transactional
    public StoredFile execute(MultipartFile multipartFile) {
        validate(multipartFile);

        StoredFile storedFile = null;

        try {
            MDC.put("event", "receipt_file_upload_started");
            log.info("Receipt storedFile upload started");

            StoredFileLocation location = fileStorageService.store(multipartFile);

            storedFile = StoredFile.create(
                location.originalFilename(),
                location.storedFilename(),
                location.contentType(),
                location.size(),
                location.storagePath()
            );

            storedFile.registerStored(Map.of(
                "originalFilename", location.originalFilename(),
                "contentType", location.contentType(),
                "size", location.size(),
                "storedFilename", location.storedFilename(),
                "storagePath", location.storagePath()
            ));

            MDC.put("fileId", storedFile.getId().toString());
            MDC.put("event", "receipt_file_ocr_processing");
            log.info("Processing receipt storedFile OCR");

            storedFile.registerOcrProcessing(Map.of(
                "ocrEngine", "tesseract",
                "language", "por+eng"
            ));

            String extractedText = ocrService.extractText(location.storagePath());

            storedFile.registerTextExtracted(
                extractedText,
                Map.of(
                    "ocrEngine", "tesseract",
                    "language", "por+eng",
                    "textLength", extractedText == null ? 0 : extractedText.length()
                )
            );

            StoredFile savedFile = storedFileRepository.save(storedFile);

            ReceiptDocumentExtractedEvent documentExtractedEvent = buildDocumentExtractedEvent(savedFile);

            OutboxEvent outboxEvent = buildOutboxEvent(savedFile, documentExtractedEvent);

            outboxEventRepository.save(outboxEvent);

            MDC.put("event", "receipt_file_processed");
            MDC.put("outboxEventId", outboxEvent.getId().toString());
            log.info("Receipt storedFile processed and outbox event saved successfully");

            return savedFile;
        } catch (Exception ex) {
            MDC.put("event", "receipt_file_processing_failed");
            log.error("Receipt storedFile processing failed", ex);

            if (storedFile == null) {
                throw new IllegalStateException("Receipt storedFile processing failed before storedFile object was created", ex);
            }
            storedFile.registerOcrFailed(Map.of(
                "errorType", ex.getClass().getSimpleName(),
                "errorMessage", safeMessage(ex)
            ));
            return storedFileRepository.save(storedFile);

        } finally {
            MDC.remove("correlationId");
            MDC.remove("fileId");
            MDC.remove("event");
            MDC.remove("outboxEventId");
        }
    }

    private ReceiptDocumentExtractedEvent buildDocumentExtractedEvent(
            StoredFile file
    ) {
        return new ReceiptDocumentExtractedEvent(
            UUID.randomUUID(),
            EVENT_VERSION,
            file.getId(),
            file.getOriginalFilename(),
            file.getContentType(),
            file.getExtractedText(),
            MDC.get("correlationId"),
            LocalDateTime.now()
        );
    }

    private OutboxEvent buildOutboxEvent(
            StoredFile file,
            ReceiptDocumentExtractedEvent event
    ) {
        return OutboxEvent.create(
            file.getId(),
            AGGREGATE_TYPE,
            EVENT_TYPE,
            receiptDocumentExtractedTopic,
            serializeEvent(event)
        );
    }

    private String serializeEvent(
            ReceiptDocumentExtractedEvent event
    ) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Failed to serialize receipt document extracted event", ex);
        }
    }

    private String safeMessage(Exception ex) {
        if (ex.getMessage() == null || ex.getMessage().isBlank()) {
            return ex.getClass().getSimpleName();
        }
        return ex.getMessage();
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("file is required");
        }

        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new IllegalArgumentException("file size exceeds the maximum allowed limit of 25MB");
        }

        String contentType = file.getContentType();

        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Only PNG, JPEG and PDF files are supported");
        }
    }
}
