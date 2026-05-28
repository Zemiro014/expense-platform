package com.jeronimo.ai_classification_service.infrastructure.ia.ollama;

import com.jeronimo.ai_classification_service.domain.model.ExpenseCategory;
import com.jeronimo.ai_classification_service.domain.model.ReceiptClassification;
import com.jeronimo.ai_classification_service.domain.service.ReceiptAiClassifier;
import com.jeronimo.ai_classification_service.infrastructure.ia.ollama.dto.OllamaGenerateRequest;
import com.jeronimo.ai_classification_service.infrastructure.ia.ollama.dto.OllamaGenerateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OllamaReceiptAiClassifier implements ReceiptAiClassifier {

    private final RestClient ollamaRestClient;

    @Value("${app.ai.ollama.model}")
    private String model;

    @Override
    public ReceiptClassification classify(
            UUID receiptId,
            String merchant,
            BigDecimal amount
    ) {
        String prompt = buildPrompt(merchant, amount);

        try {
            MDC.put("event", "ai_classification_requested");
            MDC.put("aiProvider", "ollama");
            MDC.put("aiModel", model);

            log.info("Requesting receipt classification from Ollama");

            OllamaGenerateResponse response = ollamaRestClient
                    .post()
                    .uri("/api/generate")
                    .body(new OllamaGenerateRequest(model, prompt, false))
                    .retrieve()
                    .body(OllamaGenerateResponse.class);

            String rawCategory = response == null
                    ? "OTHER"
                    : response.response();

            ExpenseCategory category = normalizeCategory(rawCategory);

            MDC.put("event", "ai_classification_completed");
            MDC.put("category", category.name());

            log.info("Receipt classified by Ollama");

            return ReceiptClassification.create(
                    receiptId,
                    merchant,
                    amount,
                    category,
                    confidenceFor(category),
                    reasonFor(category, merchant)
            );

        } catch (Exception ex) {
            MDC.put("event", "ai_classification_failed");

            log.error("Failed to classify receipt with Ollama. Falling back to OTHER", ex);

            return ReceiptClassification.create(
                    receiptId,
                    merchant,
                    amount,
                    ExpenseCategory.OTHER,
                    0.30,
                    "AI classification failed. Defaulted to OTHER"
            );

        } finally {
            MDC.remove("event");
            MDC.remove("aiProvider");
            MDC.remove("aiModel");
            MDC.remove("category");
        }
    }

    private String buildPrompt(
            String merchant,
            BigDecimal amount
    ) {
        return """
            You are an expense classification assistant.

            Classify the expense into ONLY ONE of the following categories:

            TRANSPORT
            FOOD
            TRAVEL
            OFFICE_SUPPLIES
            SOFTWARE
            HEALTH
            EDUCATION
            OTHER

            Rules:
            - Respond ONLY with the category name.
            - Do not explain.
            - Do not use markdown.
            - Do not add punctuation.
            - Do not add extra text.

            Expense:
            merchant=%s
            amount=%s
            """.formatted(merchant, amount);
    }

    private ExpenseCategory normalizeCategory(String rawResponse) {
        if (rawResponse == null || rawResponse.isBlank()) {
            return ExpenseCategory.OTHER;
        }

        String normalized = rawResponse
                .trim()
                .toUpperCase()
                .replace(".", "")
                .replace(",", "")
                .replace(" ", "_");

        try {
            return ExpenseCategory.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            return ExpenseCategory.OTHER;
        }
    }

    private Double confidenceFor(ExpenseCategory category) {
        return switch (category) {
            case TRANSPORT -> 0.92;
            case FOOD -> 0.88;
            case TRAVEL -> 0.85;
            case OFFICE_SUPPLIES -> 0.82;
            case SOFTWARE -> 0.87;
            case HEALTH -> 0.80;
            case EDUCATION -> 0.80;
            case OTHER -> 0.50;
        };
    }

    private String reasonFor(
            ExpenseCategory category,
            String merchant
    ) {
        return "AI classified merchant '%s' as %s"
                .formatted(merchant, category.name());
    }
}
