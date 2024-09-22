package br.com.uol.compass.desafio.san_giorgio.application.port.input;

import br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentItem;
import br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentStatus;

public interface SendPaymentMessageUseCase {
    void send(final PaymentStatus status, final PaymentItem domain);
}
