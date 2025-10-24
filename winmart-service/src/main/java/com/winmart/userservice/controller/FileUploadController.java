package com.winmart.userservice.controller;

import com.winmart.common.controller.BaseController;
import com.winmart.common.dto.ResponseDto;
import com.winmart.common.exception.ResponseConfig;
import com.winmart.userservice.dto.request.FileUploadRequestDTO;
import com.winmart.userservice.dto.response.FileInfoResponseDTO;
import com.winmart.userservice.dto.response.FileUploadResponseDTO;
import com.winmart.userservice.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    private final FileUploadService fileUploadService;
    protected Logger LOG = LogManager.getLogger(this.getClass());

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<FileUploadResponseDTO>> uploadFile(
            @RequestPart("file") MultipartFile file,
            @RequestPart("category") String category) {

        LOG.info("Uploading file: {} with category: {}", file.getOriginalFilename(), category);
        FileUploadResponseDTO response = fileUploadService.uploadFile(file, category);
        return ResponseConfig.success(response);
    }

    @PostMapping(value = "/upload-multiple", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseDto<List<FileUploadResponseDTO>>> uploadMultipleFiles(
            @RequestBody List<FileUploadRequestDTO> requests) {

        LOG.info("Uploading {} files", requests.size());

        // Convert to MultipartFile array
        MultipartFile[] files = requests.stream()
                .map(req -> req.getFile())
                .toArray(MultipartFile[]::new);
        String category = requests.get(0).getCategory();

        List<FileUploadResponseDTO> responses = fileUploadService.uploadFiles(files, category);
        return ResponseConfig.success(responses);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String fileName) {

        LOG.info("Downloading file: {}", fileName);
        Resource resource = fileUploadService.downloadFile(fileName);

        if (resource == null || !resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        try {
            String contentType = Files.probeContentType(resource.getFile().toPath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (Exception e) {
            LOG.error("Error downloading file: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseDto<List<FileInfoResponseDTO>>> listFiles() {
        LOG.info("Listing all uploaded files");
        List<FileInfoResponseDTO> files = fileUploadService.getAllFiles();
        return ResponseConfig.success(files);
    }

    @DeleteMapping("/{fileName}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseDto<Void>> deleteFile(@PathVariable String fileName) {

        LOG.info("Deleting file: {}", fileName);
        boolean deleted = fileUploadService.deleteFile(fileName);
        if (deleted) {
            return ResponseConfig.success(null);
        } else {
            return ResponseConfig.success(null);
        }
    }
}
