package com.winmart.common.file.excel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FieldInfo {
    private boolean hasDynamicColumn;
    private String messageErrorFieldDynamicRequire;
    private String messageErrorInterval;
    private String messageErrorEmptyData;
    private String titleColumnError;
    private String satisfactionIndexRequire;
    private String satisfactionIndexMaxRequire;
    private String satisfactionIndexInvalid;
}
