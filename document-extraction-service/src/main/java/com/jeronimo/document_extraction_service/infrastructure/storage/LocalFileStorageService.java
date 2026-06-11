package com.jeronimo.document_extraction_service.infrastructure.storage;

import com.jeronimo.document_extraction_service.domain.service.FileStorageService;
import com.jeronimo.document_extraction_service.domain.service.StoredFileLocation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Slf4j
@Component
public class LocalFileStorageService implements FileStorageService {
    private final Path basePath;

    public LocalFileStorageService(@Value("${app.storage.base-path}") String basePath) {
        this.basePath = Path.of(basePath);
    }

    @Override
    public StoredFileLocation store(MultipartFile file) {
        try {
            Files.createDirectories(basePath);

            String originalFilename = file.getOriginalFilename();
            String extension = extractExtension(originalFilename);
            String storedFilename = UUID.randomUUID() + extension;

            Path targetPath = basePath.resolve(storedFilename);

            file.transferTo(targetPath);

            MDC.put("event", "file_stored");
            MDC.put("storedFilename", storedFilename);

            log.info("File stored locally");

            return new StoredFileLocation(
                    originalFilename,
                    storedFilename,
                    file.getContentType(),
                    file.getSize(),
                    targetPath.toAbsolutePath().toString()
            );

        } catch (Exception ex) {
            throw new IllegalStateException("Failed to store file", ex);

        } finally {
            MDC.remove("event");
            MDC.remove("storedFilename");
        }
    }

    private String extractExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }

        return filename.substring(filename.lastIndexOf("."));
    }
}
