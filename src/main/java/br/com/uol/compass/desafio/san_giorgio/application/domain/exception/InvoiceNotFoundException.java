package br.com.uol.compass.desafio.san_giorgio.application.domain.exception;

import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;

public class InvoiceNotFoundException extends BusinessException {
    public InvoiceNotFoundException() {
        super("Invoice not found.", PRECONDITION_FAILED);
    }
}
