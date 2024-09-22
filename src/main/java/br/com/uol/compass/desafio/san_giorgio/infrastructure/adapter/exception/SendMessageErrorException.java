package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.exception;

public class SendMessageErrorException extends RuntimeException {
    public SendMessageErrorException() {
        super("Error to send message to the queue.");
    }
}
