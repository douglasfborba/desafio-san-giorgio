package br.com.uol.compass.desafio.san_giorgio.application.port.input;

import br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentStatus;

public interface ReceiveMessageAdapter {
    PaymentStatus getPaymentStatus();
    void receive(final String payload);
}
