package com.jeronimo.ai_classification_service.infrastructure.ai.ollama;

import com.jeronimo.ai_classification_service.domain.ai.AiInferenceClient;
import com.jeronimo.ai_classification_service.domain.ai.AiInferenceRequest;
import com.jeronimo.ai_classification_service.domain.ai.AiInferenceResult;
import com.jeronimo.ai_classification_service.domain.model.ExpenseCategory;
import com.jeronimo.ai_classification_service.domain.model.ReceiptClassification;
import com.jeronimo.ai_classification_service.domain.service.ReceiptAiClassifier;
import com.jeronimo.ai_classification_service.infrastructure.ai.ollama.dto.OllamaGenerateRequest;
import com.jeronimo.ai_classification_service.infrastructure.ai.ollama.dto.OllamaGenerateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OllamaReceiptAiClassifier implements ReceiptAiClassifier {

    private static final String USE_CASE = "receipt-category-classification";
    private static final String PROMPT_VERSION = "expense-classification-v1";

    private final AiInferenceClient aiInferenceClient;

    @Override
    public ReceiptClassification classify(
            UUID receiptId,
            String merchant,
            BigDecimal amount
    ) {
        String prompt = buildPrompt(merchant, amount);

        AiInferenceResult result = aiInferenceClient.infer(
            new AiInferenceRequest(
                USE_CASE,
                PROMPT_VERSION,
                prompt,
                Map.of(
                    "receiptId", receiptId.toString(),
                    "merchant", merchant,
                    "amount", amount
                )
            )
        );

        ExpenseCategory category =
            normalizeCategory(result.rawOutput());

        return ReceiptClassification.create(
            receiptId,
            merchant,
            amount,
            category,
            confidenceFor(category, result.fallbackUsed()),
            reasonFor(category, merchant, result)
        );
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

    private Double confidenceFor(
        ExpenseCategory category,
        boolean fallbackUsed
    ) {
        if (fallbackUsed) {
            return 0.30;
        }

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
        String merchant,
        AiInferenceResult result
    ) {
        if (result.fallbackUsed()) {
            return "AI inference failed. Defaulted to OTHER. Error: "
                    + result.errorMessage();
        }

        return "AI classified merchant '%s' as %s using model %s and prompt %s"
            .formatted(
                merchant,
                category.name(),
                result.modelName(),
                result.promptVersion()
            );
    }
}
