package br.com.uol.compass.desafio.san_giorgio.application.port.output;

import br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentItem;

public interface SendMessageAdapter {
    void send(final PaymentItem domain);
    String getQueueName();
}
