package com.daily.global.exception;

import com.daily.global.common.response.CommonResponse;
import com.daily.global.exception.dto.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("NoHandlerFoundException", ex);
        return this.makeResponseEntity(ErrorCode.HANDLER_NOT_FOUND, ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("HttpRequestMethodNotSupportedException", ex);
        return this.makeResponseEntity(ErrorCode.METHOD_NOT_ALLOWED, ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("MissingServletRequestParameterException");
        return this.makeResponseEntity(ErrorCode.INVALID_PARAMETER);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("MethodArgumentNotValidException", ex);
        return this.makeResponseEntity(ErrorCode.INVALID_PARAMETER, ex.getMessage());
    }

    private ResponseEntity<Object> makeResponseEntity(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(this.makeResponseEntityBody(errorCode, errorCode.getMessage()));
    }

    private ResponseEntity<Object> makeResponseEntity(ErrorCode errorCode, String message) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(this.makeResponseEntityBody(errorCode, message));
    }

    private CommonResponse makeResponseEntityBody(ErrorCode errorCode, String message) {
        return CommonResponse.builder()
                .code(errorCode.getHttpStatus().value())
                .name(errorCode.name())
                .message(message)
                .build();
    }

}
