package com.jeronimo.document_extraction_service.domain.repository;

import com.jeronimo.document_extraction_service.domain.model.StoredFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoredFileRepository {

    StoredFile save(StoredFile file);

    Optional<StoredFile> findById(UUID id);

    List<StoredFile> findAll();
}
