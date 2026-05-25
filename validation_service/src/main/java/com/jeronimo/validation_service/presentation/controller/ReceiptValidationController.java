package com.jeronimo.validation_service.presentation.controller;

import com.jeronimo.validation_service.application.usecase.FindAllReceiptValidation;
import com.jeronimo.validation_service.presentation.response.ReceiptValidationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/receipt-validations")
public class ReceiptValidationController {

    private final FindAllReceiptValidation findAllReceiptValidation;

    @GetMapping
    public ResponseEntity<List<ReceiptValidationResponse>> getAll(){
        List<ReceiptValidationResponse> responses = findAllReceiptValidation.execute();
        return ResponseEntity.ok(responses);
    }
}
