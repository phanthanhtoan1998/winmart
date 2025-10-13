package com.winmart.common.exception;
import com.winmart.common.Constant.MessageConstants;
import com.winmart.common.util.MessageCommon;
import lombok.extern.slf4j.Slf4j;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@Slf4j
public class ExceptionHandlingController {

     private final MessageCommon messageCommon;

    public ExceptionHandlingController(MessageCommon messageCommon) {
        this.messageCommon = messageCommon;
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    @ResponseBody
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    protected ResponseEntity handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ResponseConfig.error(INTERNAL_SERVER_ERROR,MessageConstants.SYSTEM_ERROR, messageCommon.getMessage(MessageConstants.SYSTEM_ERROR));
    }
// handle security exception
//    @ExceptionHandler(AccessDeniedException.class)
//    @ResponseBody
//    protected ResponseEntity handleAccessDeniedException(AccessDeniedException ex) {
//        return ResponseConfig.error(FORBIDDEN, MessageConstants.ACCESS_DENIED,
//                messageCommon.getMessage(MessageConstants.ACCESS_DENIED));
//    }

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    protected ResponseEntity handleBusinessException(BusinessException ex) {
        return ResponseConfig.error(BAD_REQUEST, ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseConfig.error(BAD_REQUEST, errors, MessageConstants.VALIDATE_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ResponseEntity handleSQLIntegrityConstraintViolationExceptions(DataIntegrityViolationException ex) {
        if (ex.getMessage().contains("org.hibernate.exception.ConstraintViolationException")){
            if (ex.getCause() instanceof ConstraintViolationException){
              ConstraintViolationException con = (ConstraintViolationException)ex.getCause();
                return ResponseConfig.error(NOT_ACCEPTABLE,"CONSTRAINT_ERROR", messageCommon.getMessage(con.getConstraintName()));
            }
            return ResponseConfig.error(NOT_ACCEPTABLE, ex.getMessage().split(";")[0], ex.getCause().getCause().getMessage().split("\'")[1] + " "+MessageConstants.DATA_FAIL  );
        }
        return null;
    }
    @ExceptionHandler({AccessDeniedException.class, AuthorizationDeniedException.class})
    public ResponseEntity<?> handleAccessDeniedException(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Không có quyền truy cập");
        return ResponseConfig.error(HttpStatus.FORBIDDEN, errors, String.valueOf(HttpStatus.FORBIDDEN.value()));
    }
}

