package com.jeronimo.document_extraction_service.infrastructure.persistence.mapper;

import com.jeronimo.document_extraction_service.domain.model.StoredFile;
import com.jeronimo.document_extraction_service.infrastructure.persistence.entity.StoredFileEntity;

public class StoredFileEntityMapper {

    private StoredFileEntityMapper() {
    }

    public static StoredFileEntity toEntity(StoredFile file) {
        StoredFileEntity entity = new StoredFileEntity();

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

        return entity;
    }

    public static StoredFile toDomain(StoredFileEntity entity) {
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
                .build();
    }
}
