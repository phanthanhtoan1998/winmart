package com.winmart.common.file.excel;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class ResultReadExcel<T> {
    private List<T> result = new ArrayList<>();
    private Boolean invalidData;
    private String errorMessage;
    private Map<Integer, List<ImageData>> imagesByRow = new HashMap<>();

}
