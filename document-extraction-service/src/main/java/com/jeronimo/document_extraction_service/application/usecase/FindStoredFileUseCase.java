package com.jeronimo.document_extraction_service.application.usecase;

import com.jeronimo.document_extraction_service.domain.model.StoredFile;
import com.jeronimo.document_extraction_service.domain.repository.StoredFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindStoredFileUseCase {

    private final StoredFileRepository repository;

    public List<StoredFile> findAll() {
        return repository.findAll();
    }

    public Optional<StoredFile> findById(UUID id) {
        return repository.findById(id);
    }
}
