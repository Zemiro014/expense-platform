package com.jeronimo.validation_service.infrastructure.persistence.entity;

import com.jeronimo.validation_service.domain.model.ValidationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "receipt_validations")
public class ReceiptValidationEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID receiptId;

    @Column(nullable = false)
    private String merchant;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ValidationStatus status;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private LocalDateTime validatedAt;
}
