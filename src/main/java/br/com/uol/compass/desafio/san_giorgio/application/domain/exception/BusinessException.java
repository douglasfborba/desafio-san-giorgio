package br.com.uol.compass.desafio.san_giorgio.application.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static lombok.AccessLevel.PROTECTED;

@Getter
@RequiredArgsConstructor(access = PROTECTED)
public abstract class BusinessException extends RuntimeException {
    private final String message;
    private final HttpStatus status;
}
