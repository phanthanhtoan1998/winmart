package com.winmart.common.exception;

import com.winmart.common.dto.ResponseDto;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;

public class ResponseConfig<T> {

    public static final String SUCCESS_CODE = "00";
    public static final String ERROR_CODE = "01";

    public static <T> ResponseEntity<ResponseDto<T>> success(T body) {
        ResponseDto responseDto = ResponseDto.builder().data(body).code(SUCCESS_CODE).build();
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    public static <T> ResponseEntity<ResponseDto<T>> success(T body, String message) {
        ResponseDto responseDto = ResponseDto.builder().data(body).code(SUCCESS_CODE).message(message).build();
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    public static <T> ResponseEntity<ResponseDto<T>> badRequest(String message) {
        ResponseDto responseDto = ResponseDto.builder().code(ERROR_CODE).message(message).build();
        return new ResponseEntity(responseDto, HttpStatus.BAD_REQUEST);
    }

    public static <T> ResponseEntity<ResponseDto<T>> successDelete(T body, Boolean success) {
        if(success){
            ResponseDto responseDto = ResponseDto.builder().data(body).code(SUCCESS_CODE).build();
            return new ResponseEntity(responseDto, HttpStatus.OK);
        } else {
            ResponseDto responseDto = ResponseDto.builder().data(body).code(ERROR_CODE).build();
            return new ResponseEntity(responseDto, HttpStatus.OK);
        }
    }

    public static ResponseEntity error(HttpStatus httpStatus, String errorCode, String message) {
        ResponseDto responseData = ResponseDto.builder().code(errorCode).message(message).build();
        return new ResponseEntity(responseData, httpStatus);
    }

    public static <T> ResponseEntity<T> error(HttpStatus httpStatus, T content, String code) {
        ResponseDto responseData = ResponseDto.builder().code(code).data(content).build();
        return new ResponseEntity(responseData, httpStatus);
    }
//
    /**
     * download file to client
     * @param fileName name of download file
     * @param input stream file download
     * @return response
     */
    public static ResponseEntity<InputStreamResource> downloadFile(String fileName, InputStreamResource input) {
        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                // Content-Type
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(input);
    }

    /**
     * download file to client
     * @param fileName name of download file
     * @param outputStream stream file download
     * @return response
     */
    public static ResponseEntity<InputStreamResource> downloadFile(String fileName, ByteArrayInputStream outputStream) {
        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                // Content-Type
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body( new InputStreamResource(outputStream));
    }
}
