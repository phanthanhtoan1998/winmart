package com.winmart.common.file.excel;

import lombok.Builder;
import lombok.Data;

import java.util.function.Predicate;

@Data
@Builder
public class CheckDataField {
    private Predicate<String> predicate;
    private String errorMessage;
}
