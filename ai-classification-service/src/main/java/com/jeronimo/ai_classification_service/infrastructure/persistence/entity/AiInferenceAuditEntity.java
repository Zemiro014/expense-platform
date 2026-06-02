package com.jeronimo.ai_classification_service.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "ai_inference_audits")
public class AiInferenceAuditEntity {

    @Id
    private UUID id;

    @Column(name = "receipt_id", nullable = false)
    private UUID receiptId;

    @Column(name = "use_case", nullable = false)
    private String useCase;

    @Column(name = "model_name", nullable = false)
    private String modelName;

    @Column(name = "prompt_version", nullable = false)
    private String promptVersion;

    @Column(name = "raw_output", length = 2000)
    private String rawOutput;

    @Column(name = "normalized_output")
    private String normalizedOutput;

    @Column(name = "latency_ms")
    private Long latencyMs;

    @Column(name = "fallback_used")
    private Boolean fallbackUsed;

    @Column(name = "error_message", length = 2000)
    private String errorMessage;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
