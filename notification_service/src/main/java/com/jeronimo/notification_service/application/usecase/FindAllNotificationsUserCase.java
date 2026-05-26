package com.jeronimo.notification_service.application.usecase;

import com.jeronimo.notification_service.application.mapper.NotificationMapper;
import com.jeronimo.notification_service.domain.repository.NotificationRepository;
import com.jeronimo.notification_service.presentation.response.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindAllNotificationsUserCase {
    private final NotificationRepository repository;

    public List<NotificationResponse> execute(){
        return repository.findAll()
                .stream()
                .map(NotificationMapper::toResponse)
                .toList();
    }
}
