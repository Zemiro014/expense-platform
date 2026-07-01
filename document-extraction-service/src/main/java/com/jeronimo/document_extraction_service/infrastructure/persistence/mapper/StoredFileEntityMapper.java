package com.jeronimo.document_extraction_service.infrastructure.persistence.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jeronimo.document_extraction_service.domain.event.StoredFileEvent;
import com.jeronimo.document_extraction_service.domain.model.StoredFile;
import com.jeronimo.document_extraction_service.infrastructure.persistence.entity.StoredFileEntity;

import java.util.List;

public class StoredFileEntityMapper {

    private StoredFileEntityMapper() {
    }

    public static StoredFileEntity toEntity(StoredFile file) {
        StoredFileEntity entity = new StoredFileEntity();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        entity.setId(file.getId());
        entity.setOriginalFilename(file.getOriginalFilename());
        entity.setStoredFilename(file.getStoredFilename());
        entity.setContentType(file.getContentType());
        entity.setSize(file.getSize());
        entity.setStoragePath(file.getStoragePath());
        entity.setStatus(file.getStatus());
        entity.setExtractedText(file.getExtractedText());
        entity.setErrorMessage(file.getErrorMessage());
        entity.setCreatedAt(file.getCreatedAt());
        entity.setUpdatedAt(file.getUpdatedAt());
        entity.setDocumentExtractionEventsProcess(objectMapper.valueToTree(file.getDocumentExtractionHistoryProcess()));

        return entity;
    }

    public static StoredFile toDomain(StoredFileEntity entity) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        List<StoredFileEvent> documentExtractionEventsProcess =
                objectMapper.convertValue(
                        entity.getDocumentExtractionEventsProcess(),
                        new TypeReference<List<StoredFileEvent>>() {}
                );

        return StoredFile.builder()
                .id(entity.getId())
                .originalFilename(entity.getOriginalFilename())
                .storedFilename(entity.getStoredFilename())
                .contentType(entity.getContentType())
                .size(entity.getSize())
                .storagePath(entity.getStoragePath())
                .status(entity.getStatus())
                .extractedText(entity.getExtractedText())
                .errorMessage(entity.getErrorMessage())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .documentExtractionHistoryProcess(documentExtractionEventsProcess)
                .build();
    }
}
