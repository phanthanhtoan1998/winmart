package com.winmart.userservice.dto.request;

import com.winmart.common.dto.BaseDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileUploadRequestDTO extends BaseDto {
    
    @NotBlank(message = "File is required")
    private MultipartFile file;
    
    @NotBlank(message = "Category is required")
    private String category;
}
