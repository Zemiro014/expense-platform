package com.jeronimo.ai_classification_service.infrastructure.persistence.repository;

import com.jeronimo.ai_classification_service.infrastructure.persistence.entity.AiInferenceAuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SpringDataAiInferenceAuditRepository extends JpaRepository<AiInferenceAuditEntity, UUID> {
    List<AiInferenceAuditEntity> findByReceiptId(UUID receiptId);
    long countByFallbackUsedTrue();

    long countByErrorMessageIsNotNull();

    @Query("SELECT AVG(a.latencyMs) FROM AiInferenceAuditEntity a")
    Double averageLatencyMs();

    @Query("""
           SELECT a.modelName
           FROM AiInferenceAuditEntity a
           GROUP BY a.modelName
           ORDER BY COUNT(a.modelName) DESC
           LIMIT 1
           """)
    String findMostUsedModel();
}
