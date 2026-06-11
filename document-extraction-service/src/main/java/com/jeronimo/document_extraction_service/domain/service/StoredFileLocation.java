package com.jeronimo.document_extraction_service.domain.service;

public record StoredFileLocation(
    String originalFilename,
    String storedFilename,
    String contentType,
    Long size,
    String storagePath
) { }
