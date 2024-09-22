package br.com.uol.compass.desafio.san_giorgio.application.port.input;

import br.com.uol.compass.desafio.san_giorgio.application.domain.model.Payment;
import br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentStatus;

public interface UpdatePaymentStatusUseCase {
    Payment update(final PaymentStatus status, final Payment domain);
}
