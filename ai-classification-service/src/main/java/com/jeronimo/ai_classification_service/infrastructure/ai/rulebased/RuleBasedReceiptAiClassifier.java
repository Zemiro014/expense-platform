package com.jeronimo.ai_classification_service.infrastructure.ai.rulebased;

import com.jeronimo.ai_classification_service.domain.model.ExpenseCategory;
import com.jeronimo.ai_classification_service.domain.model.ReceiptClassification;
import com.jeronimo.ai_classification_service.domain.service.ReceiptAiClassifier;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Component("ruleBasedReceiptAiClassifier")
public class RuleBasedReceiptAiClassifier implements ReceiptAiClassifier {

    @Override
    public ReceiptClassification classify(
            UUID receiptId,
            String merchant,
            BigDecimal amount
    ) {
        String normalizedMerchant = normalizeMerchant(merchant);

        ExpenseCategory category = classifyByMerchant(normalizedMerchant);
        double confidence = confidenceFor(category);
        String reason = reasonFor(category, merchant);

        MDC.put("event", "rule_based_classification_completed");
        MDC.put("aiProvider", "rule-based");
        MDC.put("category", category.name());
        MDC.put("confidence", String.valueOf(confidence));

        try {
            log.info("Receipt classified using rule-based fallback");

            return ReceiptClassification.create(
                    receiptId,
                    merchant,
                    amount,
                    category,
                    confidence,
                    reason,
                    null
            );

        } finally {
            MDC.remove("event");
            MDC.remove("aiProvider");
            MDC.remove("category");
            MDC.remove("confidence");
        }
    }

    private String normalizeMerchant(String merchant) {
        if (merchant == null || merchant.isBlank()) {
            return "";
        }

        return merchant
                .trim()
                .toLowerCase();
    }

    private ExpenseCategory classifyByMerchant(String merchant) {
        if (containsAny(merchant, "uber", "99", "taxi", "cabify", "bolt")) {
            return ExpenseCategory.TRANSPORT;
        }

        if (containsAny(merchant, "ifood", "restaurant", "mcdonald", "burger", "pizza", "starbucks")) {
            return ExpenseCategory.FOOD;
        }

        if (containsAny(merchant, "hotel", "booking", "airbnb", "latam", "gol", "azul", "flight")) {
            return ExpenseCategory.TRAVEL;
        }

        if (containsAny(merchant, "amazon", "office", "kalunga", "staples")) {
            return ExpenseCategory.OFFICE_SUPPLIES;
        }

        if (containsAny(merchant, "github", "aws", "google cloud", "azure", "openai", "jetbrains", "software")) {
            return ExpenseCategory.SOFTWARE;
        }

        if (containsAny(merchant, "pharmacy", "hospital", "clinic", "drogaria")) {
            return ExpenseCategory.HEALTH;
        }

        if (containsAny(merchant, "udemy", "coursera", "alura", "book", "school", "course")) {
            return ExpenseCategory.EDUCATION;
        }

        return ExpenseCategory.OTHER;
    }

    private boolean containsAny(String value, String... terms) {
        for (String term : terms) {
            if (value.contains(term)) {
                return true;
            }
        }

        return false;
    }

    private double confidenceFor(ExpenseCategory category) {
        return switch (category) {
            case TRANSPORT -> 0.85;
            case FOOD -> 0.80;
            case TRAVEL -> 0.78;
            case OFFICE_SUPPLIES -> 0.75;
            case SOFTWARE -> 0.82;
            case HEALTH -> 0.74;
            case EDUCATION -> 0.74;
            case OTHER -> 0.40;
        };
    }

    private String reasonFor(
            ExpenseCategory category,
            String merchant
    ) {
        if (category == ExpenseCategory.OTHER) {
            return "Rule-based fallback could not confidently classify merchant '%s'"
                    .formatted(merchant);
        }

        return "Rule-based fallback classified merchant '%s' as %s"
                .formatted(merchant, category.name());
    }
}
