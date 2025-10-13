package com.winmart.common.file.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.CellType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CellValue {
    private Integer index;
    private String stringValue;
    private Object rawValue;
    private CellType cellType;
    private boolean wrongTypeColumn;
}
