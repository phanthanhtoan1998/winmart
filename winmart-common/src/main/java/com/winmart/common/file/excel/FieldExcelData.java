package com.winmart.common.file.excel;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FieldExcelData {
    private String fieldName;
    private boolean primaryKey;
    private boolean notEmpty;
    private String errorEmpty;
    private List<CheckDataField> checkDataFieldList;
}
