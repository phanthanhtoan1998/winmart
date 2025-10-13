package com.winmart.common.dto;

import com.winmart.common.model.SortField;
import lombok.Data;

import java.util.List;

@Data
public class BaseDto {
    private Integer pageNumber;
    private Integer pageSize;
    private List<SortField> sortFields;
}
