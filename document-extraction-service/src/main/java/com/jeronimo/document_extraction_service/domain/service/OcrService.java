package com.jeronimo.document_extraction_service.domain.service;

public interface OcrService {
    String extractText(String filePath);
}