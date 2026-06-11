package com.jeronimo.document_extraction_service.infrastructure.ocr;

import com.jeronimo.document_extraction_service.domain.service.OcrService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class TesseractOcrService implements OcrService {
    @Override
    public String extractText(String filePath) {
        try {
            MDC.put("event", "ocr_started");
            log.info("Starting OCR extraction");

            ProcessBuilder processBuilder = new ProcessBuilder(
                    "tesseract",
                    filePath,
                    "stdout",
                    "-l",
                    "por+eng"
            );

            Process process = processBuilder.start();
            boolean finished = process.waitFor(30, TimeUnit.SECONDS);

            if (!finished) {
                process.destroyForcibly();
                throw new IllegalStateException("OCR process timeout");
            }

            String output = new String(
                    process.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            String error = new String(
                    process.getErrorStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            if (process.exitValue() != 0) {
                throw new IllegalStateException("OCR failed: " + error);
            }

            MDC.put("event", "ocr_completed");
            log.info("OCR extraction completed");
            return output.trim();
        } catch (Exception ex) {
            MDC.put("event", "ocr_failed");
            log.error("OCR extraction failed", ex);
            throw new IllegalStateException("Failed to extract text from file", ex);
        } finally {
            MDC.remove("event");
        }
    }
}
