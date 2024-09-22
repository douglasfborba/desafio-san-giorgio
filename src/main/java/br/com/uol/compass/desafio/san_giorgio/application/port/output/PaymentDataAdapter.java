package br.com.uol.compass.desafio.san_giorgio.application.port.output;

import br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentItem;

public interface PaymentDataAdapter {
    PaymentItem save(final PaymentItem item);
}
