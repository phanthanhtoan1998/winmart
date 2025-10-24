package com.winmart.userservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.winmart.common.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)  // Don't include null fields in response
public class CategoryResponse extends BaseDto {
    private Long id;
    private String name;
    private String description;
    
    // Parent-Child relationship
    private Long parentId;
    private String parentName;

    // Children (only included when explicitly requested)
    private List<CategoryResponse> children;
}
