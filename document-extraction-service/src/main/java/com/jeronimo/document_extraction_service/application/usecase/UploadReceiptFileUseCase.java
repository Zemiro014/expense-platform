package com.jeronimo.document_extraction_service.application.usecase;

import com.jeronimo.document_extraction_service.domain.event.ReceiptDocumentExtractedEvent;
import com.jeronimo.document_extraction_service.domain.model.StoredFile;
import com.jeronimo.document_extraction_service.domain.publisher.ReceiptDocumentExtractedEventPublisher;
import com.jeronimo.document_extraction_service.domain.repository.StoredFileRepository;
import com.jeronimo.document_extraction_service.domain.service.FileStorageService;
import com.jeronimo.document_extraction_service.domain.service.OcrService;
import com.jeronimo.document_extraction_service.domain.service.StoredFileLocation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadReceiptFileUseCase {

    private final FileStorageService fileStorageService;
    private final OcrService ocrService;
    private final StoredFileRepository storedFileRepository;
    private final ReceiptDocumentExtractedEventPublisher eventPublisher;

    public StoredFile execute(
            MultipartFile multipartFile,
            String correlationId
    ) {
        validate(multipartFile);

        StoredFileLocation location = fileStorageService.store(multipartFile);

        StoredFile file = StoredFile.uploaded(
            location.originalFilename(),
            location.storedFilename(),
            location.contentType(),
            location.size(),
            location.storagePath()
        ).markStored();

        StoredFile savedFile = storedFileRepository.save(file);

        try {
            MDC.put("fileId", savedFile.getId().toString());
            MDC.put("event", "receipt_file_ocr_processing");
            log.info("Processing receipt file OCR");

            StoredFile processingFile = savedFile.markOcrProcessing();

            storedFileRepository.save(processingFile);

            String extractedText = ocrService.extractText(savedFile.getStoragePath());

            StoredFile extractedFile = savedFile.markTextExtracted(extractedText);

            StoredFile finalFile = storedFileRepository.save(extractedFile);

            ReceiptDocumentExtractedEvent event =
                new ReceiptDocumentExtractedEvent(
                    UUID.randomUUID(),
                    1,
                    finalFile.getId(),
                    finalFile.getOriginalFilename(),
                    finalFile.getContentType(),
                    finalFile.getExtractedText(),
                    correlationId,
                    LocalDateTime.now()
                );

            eventPublisher.publish(event);

            MDC.put("event", "receipt_file_processed");
            log.info("Receipt file processed successfully");

            return finalFile;

        } catch (Exception ex) {
            StoredFile failedFile = savedFile.markOcrFailed(ex.getMessage());
            StoredFile finalFile = storedFileRepository.save(failedFile);

            MDC.put("event", "receipt_file_processing_failed");
            log.error("Receipt file processing failed", ex);

            return finalFile;
        } finally {
            MDC.remove("fileId");
            MDC.remove("event");
        }
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("file is required");
        }

        String contentType = file.getContentType();

        if (contentType == null ||
            !(contentType.equals("image/png")
                || contentType.equals("image/jpeg")
                || contentType.equals("application/pdf"))) {

            throw new IllegalArgumentException("Only PNG, JPEG and PDF files are supported");
        }
    }
}
