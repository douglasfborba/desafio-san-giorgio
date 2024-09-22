package br.com.uol.compass.desafio.san_giorgio.application.domain.exception;

import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;

public class SellerNotFoundException extends BusinessException {
    public SellerNotFoundException() {
        super("Seller not found.", PRECONDITION_FAILED);
    }
}
