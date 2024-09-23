package br.com.uol.compass.desafio.san_giorgio.application.domain.service;

import br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentItem;
import br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentStatus;
import br.com.uol.compass.desafio.san_giorgio.application.port.input.SendPaymentMessageUseCase;
import br.com.uol.compass.desafio.san_giorgio.application.port.output.SendMessageAdapter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class PaymentMessageSenderService implements SendPaymentMessageUseCase {

    private final Map<String, SendMessageAdapter> sendMessageAdapters;

    @Override
    public void send(final PaymentStatus paymentStatus, final PaymentItem domain) {
        sendMessageAdapters
                .get(paymentStatus.name().toLowerCase())
                .send(domain);
    }
}
