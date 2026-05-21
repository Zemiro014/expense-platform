package com.jeronimo.receipt_service.infrastructure.persistence.entity;

import com.jeronimo.receipt_service.domain.model.ReceiptStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "receipts")
public class ReceiptEntity implements Persistable<UUID> {
    @Id
    private UUID id;
    private String merchant;
    private BigDecimal amount;
    private ReceiptStatus status;
    private LocalDateTime createdAt;

    @Transient
    private boolean isNew = true;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void markNotNew() {
        this.isNew = false;
    }
}
