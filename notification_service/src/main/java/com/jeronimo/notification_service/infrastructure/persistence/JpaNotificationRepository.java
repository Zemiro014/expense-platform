package com.jeronimo.notification_service.infrastructure.persistence;

import com.jeronimo.notification_service.domain.model.Notification;
import com.jeronimo.notification_service.domain.repository.NotificationRepository;
import com.jeronimo.notification_service.infrastructure.persistence.entity.NotificationEntity;
import com.jeronimo.notification_service.infrastructure.persistence.mapper.NotificationEntityMapper;
import com.jeronimo.notification_service.infrastructure.persistence.repository.SpringDataNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaNotificationRepository implements NotificationRepository {

    private final SpringDataNotificationRepository repository;

    @Override
    public Notification save(Notification notification) {
        NotificationEntity entity =
                NotificationEntityMapper.toEntity(notification);

        NotificationEntity savedEntity =
                repository.save(entity);

        return NotificationEntityMapper.toDomain(savedEntity);
    }

    @Override
    public boolean existsByReceiptId(UUID receiptId) {
        return repository.existsByReceiptId(receiptId);
    }

    @Override
    public List<Notification> findAll() {
        return repository.findAll()
                .stream().map(NotificationEntityMapper::toDomain)
                .toList();
    }
}
