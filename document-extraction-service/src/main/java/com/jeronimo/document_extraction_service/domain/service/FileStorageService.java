package com.jeronimo.document_extraction_service.domain.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    StoredFileLocation store(MultipartFile file);
}
