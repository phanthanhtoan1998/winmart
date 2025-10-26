package com.winmart.userservice.service.impl;

import com.winmart.userservice.dto.response.FileInfoResponseDTO;
import com.winmart.userservice.dto.response.FileUploadResponseDTO;
import com.winmart.userservice.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${file.upload-dir:/home/toan/Documents/winmart/uploads}")
    private String uploadDir;
    
    @Value("${file.base-url:http://localhost:3333/api/files/download?fileName=}")
    private String baseUrl;

    @Override
    public FileUploadResponseDTO uploadFile(MultipartFile file, String category) {
        try {
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir, category);
            Files.createDirectories(uploadPath);

            // Generate unique filename
            String originalFileName = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

            // Save file
            Path filePath = uploadPath.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), filePath);

            // Build response
            return FileUploadResponseDTO.builder()
                    .fileName(uniqueFileName)
                    .originalFileName(originalFileName)
                    .filePath(filePath.toString())
                    .fileUrl(baseUrl + uniqueFileName)
                    .category(category)
                    .fileSize(file.getSize())
                    .contentType(file.getContentType())
                    .uploadedAt(LocalDateTime.now())
                    .build();

        } catch (IOException e) {
            log.error("Error uploading file: {}", e.getMessage());
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }

    @Override
    public List<FileUploadResponseDTO> uploadFiles(MultipartFile[] files, String category) {
        List<FileUploadResponseDTO> responses = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                responses.add(uploadFile(file, category));
            }
        }

        return responses;
    }

    @Override
    public Resource downloadFile(String fileName) {
        // Search for file in all category directories
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (Files.exists(uploadPath)) {
                return Files.walk(uploadPath)
                        .filter(Files::isRegularFile)
                        .filter(path -> path.getFileName().toString().equals(fileName))
                        .findFirst()
                        .map(path -> new FileSystemResource(path.toFile()))
                        .orElse(null);
            }
        } catch (IOException e) {
            log.error("Error searching for file: {}", e.getMessage());
        }
        return null;
    }

    @Override
    public List<FileInfoResponseDTO> getAllFiles() {
        List<FileInfoResponseDTO> files = new ArrayList<>();

        try {
            Path uploadPath = Paths.get(uploadDir);
            if (Files.exists(uploadPath)) {
                Files.walk(uploadPath)
                        .filter(Files::isRegularFile)
                        .forEach(path -> {
                            try {
                                String fileName = path.getFileName().toString();
                                String category = path.getParent().getFileName().toString();

                                FileInfoResponseDTO fileInfo = FileInfoResponseDTO.builder()
                                        .fileName(fileName)
                                        .originalFileName(fileName) // In real implementation, you might store original name
                                        .filePath(path.toString())
                                        .fileUrl(baseUrl + fileName)
                                        .category(category)
                                        .fileSize(Files.size(path))
                                        .contentType(Files.probeContentType(path))
                                        .uploadedAt(LocalDateTime.now()) // In real implementation, you might store this info
                                        .lastModified(LocalDateTime.ofInstant(
                                                Files.getLastModifiedTime(path).toInstant(),
                                                java.time.ZoneId.systemDefault()))
                                        .build();

                                files.add(fileInfo);
                            } catch (IOException e) {
                                log.error("Error reading file info: {}", e.getMessage());
                            }
                        });
            }
        } catch (IOException e) {
            log.error("Error listing files: {}", e.getMessage());
        }

        return files;
    }

    @Override
    public boolean deleteFile(String fileName) {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (Files.exists(uploadPath)) {
                return Files.walk(uploadPath)
                        .filter(Files::isRegularFile)
                        .filter(path -> path.getFileName().toString().equals(fileName))
                        .findFirst()
                        .map(path -> {
                            try {
                                Files.delete(path);
                                return true;
                            } catch (IOException e) {
                                log.error("Error deleting file: {}", e.getMessage());
                                return false;
                            }
                        })
                        .orElse(false);
            }
            return false;
        } catch (IOException e) {
            log.error("Error deleting file: {}", e.getMessage());
            return false;
        }
    }
}
