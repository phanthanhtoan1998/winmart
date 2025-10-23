package com.winmart.userservice.dto.response;

import com.winmart.common.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileInfoResponseDTO extends BaseDto {
    private String fileName;
    private String originalFileName;
    private String filePath;
    private String fileUrl;
    private String category;
    private Long fileSize;
    private String contentType;
    private LocalDateTime uploadedAt;
    private LocalDateTime lastModified;
}
