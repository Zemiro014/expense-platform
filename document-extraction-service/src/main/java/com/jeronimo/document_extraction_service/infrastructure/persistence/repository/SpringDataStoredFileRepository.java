package com.jeronimo.document_extraction_service.infrastructure.persistence.repository;

import com.jeronimo.document_extraction_service.infrastructure.persistence.entity.StoredFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataStoredFileRepository extends JpaRepository<StoredFileEntity, UUID> {
}
