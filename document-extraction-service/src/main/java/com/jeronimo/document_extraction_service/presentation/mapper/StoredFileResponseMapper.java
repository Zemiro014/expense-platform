package com.jeronimo.document_extraction_service.presentation.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jeronimo.document_extraction_service.domain.model.StoredFile;
import com.jeronimo.document_extraction_service.presentation.response.StoredFileResponse;

public class StoredFileResponseMapper {

    private StoredFileResponseMapper() {
    }

    public static StoredFileResponse toResponse(StoredFile file) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return new StoredFileResponse(
            file.getId(),
            file.getOriginalFilename(),
            file.getContentType(),
            file.getSize(),
            file.getStatus(),
            file.getExtractedText(),
            file.getErrorMessage(),
            file.getCreatedAt(),
            file.getUpdatedAt(),
                objectMapper.valueToTree(file.getDocumentExtractionHistoryProcess())
        );
    }
}
