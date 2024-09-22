package br.com.uol.compass.desafio.san_giorgio.application.port.input;

import br.com.uol.compass.desafio.san_giorgio.application.domain.model.Payment;

public interface ConfirmPaymentUseCase {
    Payment confirm(final Payment domain);
}
