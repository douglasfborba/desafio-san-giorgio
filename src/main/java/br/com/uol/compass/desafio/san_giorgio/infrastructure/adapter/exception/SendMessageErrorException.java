package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.exception;

import br.com.uol.compass.desafio.san_giorgio.application.domain.exception.BusinessException;

import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;

public class SendMessageErrorException extends BusinessException {
    public SendMessageErrorException() {
        super("Error to send message to the queue.", PRECONDITION_FAILED);
    }
}
