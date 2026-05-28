package com.jeronimo.ai_classification_service.infrastructure.persistence.entity;

import com.jeronimo.ai_classification_service.domain.model.ExpenseCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "receipt_classifications")
public class ReceiptClassificationEntity {

    @Id
    private UUID id;

    @Column(name = "receipt_id", nullable = false, unique = true)
    private UUID receiptId;

    @Column(nullable = false)
    private String merchant;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpenseCategory category;

    @Column(nullable = false)
    private Double confidence;

    @Column(nullable = false, length = 1000)
    private String reason;

    @Column(name = "classified_at", nullable = false)
    private LocalDateTime classifiedAt;
}
