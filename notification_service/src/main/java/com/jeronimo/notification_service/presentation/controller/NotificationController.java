package com.jeronimo.notification_service.presentation.controller;

import com.jeronimo.notification_service.application.usecase.FindAllNotificationsUserCase;
import com.jeronimo.notification_service.presentation.response.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/receipt-notifications")
public class NotificationController {
    private final FindAllNotificationsUserCase findAllNotificationsUserCase;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> findAll(){
        return ResponseEntity.ok(findAllNotificationsUserCase.execute());
    }
}
