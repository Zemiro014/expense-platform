package com.jeronimo.validation_service.application.usecase;

import com.jeronimo.validation_service.application.mapper.ReceiptValidationMapper;
import com.jeronimo.validation_service.domain.repository.ReceiptValidationRepository;
import com.jeronimo.validation_service.presentation.response.ReceiptValidationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class FindAllReceiptValidation {

    private final ReceiptValidationRepository validationRepository;

    public List<ReceiptValidationResponse> execute() {
        return validationRepository.findAll()
                .stream().map(ReceiptValidationMapper::toResponse)
                .toList();
    }
}
