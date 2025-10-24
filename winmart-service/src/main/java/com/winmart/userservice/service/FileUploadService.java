package com.winmart.userservice.service;

import com.winmart.userservice.dto.response.FileInfoResponseDTO;
import com.winmart.userservice.dto.response.FileUploadResponseDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadService {
    
    /**
     * Upload a single file
     * @param file The file to upload
     * @param category The category of the file
     * @return FileUploadResponseDTO with upload details
     */
    FileUploadResponseDTO uploadFile(MultipartFile file, String category);
    
    /**
     * Upload multiple files
     * @param files Array of files to upload
     * @param category The category of the files
     * @return List of FileUploadResponseDTO with upload details
     */
    List<FileUploadResponseDTO> uploadFiles(MultipartFile[] files, String category);
    
    /**
     * Download a file by filename
     * @param fileName The name of the file to download
     * @return Resource containing the file data
     */
    Resource downloadFile(String fileName);
    
    /**
     * Get all uploaded files information
     * @return List of FileInfoResponseDTO
     */
    List<FileInfoResponseDTO> getAllFiles();
    
    /**
     * Delete a file by filename
     * @param fileName The name of the file to delete
     * @return true if deleted successfully, false otherwise
     */
    boolean deleteFile(String fileName);
}
