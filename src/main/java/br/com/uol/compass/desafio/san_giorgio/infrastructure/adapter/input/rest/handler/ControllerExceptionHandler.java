package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.input.rest.handler;

import br.com.uol.compass.desafio.san_giorgio.application.domain.exception.BusinessException;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.input.rest.response.ErrorItemResponse;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.input.rest.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.Timestamp;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception exception,
                                                         final HttpServletRequest request) {
        log.error("Exception with message: {}", exception.getMessage(), exception);
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponse(
                                new Timestamp(System.currentTimeMillis()),
                                INTERNAL_SERVER_ERROR.value(),
                                request.getRequestURI(),
                                List.of(new ErrorItemResponse(null, "Error to process request."))
                        )
                );
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException exception,
                                                                 final HttpServletRequest request) {
        log.error("BusinessException with message: {}", exception.getMessage(), exception);
        return ResponseEntity
                .status(exception.getStatus())
                .body(
                        new ErrorResponse(
                                new Timestamp(System.currentTimeMillis()),
                                exception.getStatus().value(),
                                request.getRequestURI(),
                                List.of(new ErrorItemResponse(null, exception.getMessage()))
                        )
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception,
                                                                               final HttpServletRequest request) {
        log.error("MethodArgumentNotValidException with message: {}", exception.getMessage(), exception);
        var errors = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorItemResponse(error.getField(), error.getDefaultMessage()))
                .toList();

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        new ErrorResponse(
                                new Timestamp(System.currentTimeMillis()),
                                BAD_REQUEST.value(),
                                request.getRequestURI(),
                                errors
                        )
                );
    }
}
