package com.winmart.common.file.excel;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class DataImportExcel<T> {
    private List<T> fileData = new ArrayList<>();
    private List<String> dynamicField = new ArrayList<>();
    private ByteArrayOutputStream rawData;
    private boolean hasError;
}
