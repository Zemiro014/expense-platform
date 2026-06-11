package com.jeronimo.document_extraction_service.infrastructure.persistence;

import com.jeronimo.document_extraction_service.domain.model.StoredFile;
import com.jeronimo.document_extraction_service.domain.repository.StoredFileRepository;
import com.jeronimo.document_extraction_service.infrastructure.persistence.mapper.StoredFileEntityMapper;
import com.jeronimo.document_extraction_service.infrastructure.persistence.repository.SpringDataStoredFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaStoredFileRepository implements StoredFileRepository {

    private final SpringDataStoredFileRepository storedFileRepository;

    @Override
    public StoredFile save(StoredFile file) {
        return StoredFileEntityMapper.toDomain(
                storedFileRepository.save(StoredFileEntityMapper.toEntity(file))
        );
    }

    @Override
    public Optional<StoredFile> findById(UUID id) {
        return storedFileRepository.findById(id)
                .map(StoredFileEntityMapper::toDomain);
    }

    @Override
    public List<StoredFile> findAll() {
        return storedFileRepository.findAll()
                .stream()
                .map(StoredFileEntityMapper::toDomain)
                .toList();
    }
}
