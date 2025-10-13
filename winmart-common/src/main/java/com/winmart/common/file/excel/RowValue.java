package com.winmart.common.file.excel;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RowValue {
    private List<CellValue> record;
    private String error;
}
