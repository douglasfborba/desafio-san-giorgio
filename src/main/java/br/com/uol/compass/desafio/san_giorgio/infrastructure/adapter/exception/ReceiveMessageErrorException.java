package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.exception;

public class ReceiveMessageErrorException extends RuntimeException {
    public ReceiveMessageErrorException() {
        super("Error to receive message from the queue.");
    }
}
