package com.winmart.common.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDto<T> implements Serializable {

    private String code;
    private String message;
    private T data;
}
