package com.winmart.common.file.excel;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Enhanced result container that holds parsed records and images per row.
 */
@Data
public class ResultReadAllExcel<T> {
    private boolean invalidData;
    private String errorMessage;
    private final List<T> records = new ArrayList<>();
    private final Map<Integer, List<ImageData>> imagesByRow = new HashMap<>();


    public void setImagesByRow(Map<Integer, List<ImageData>> map) {
        imagesByRow.clear();
        imagesByRow.putAll(map);
    }
}