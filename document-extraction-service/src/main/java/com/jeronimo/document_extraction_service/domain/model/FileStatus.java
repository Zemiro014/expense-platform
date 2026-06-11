package com.jeronimo.document_extraction_service.domain.model;

public enum FileStatus {
    UPLOADED,
    STORED,
    OCR_PROCESSING,
    TEXT_EXTRACTED,
    OCR_FAILED
}
