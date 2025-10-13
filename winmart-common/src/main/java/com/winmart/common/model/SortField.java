package com.winmart.common.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SortField {
    String fieldName;
    String sort;

    public SortField(String fieldName, String sort) {
        this.fieldName = fieldName;
        this.sort = sort;
    }
}
