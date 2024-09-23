package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.input.factory.message;

import br.com.uol.compass.desafio.san_giorgio.application.factory.model.PaymentItemFactory;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.queue.message.PaymentMessage;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public class PaymentMessageFactory {

    public static PaymentMessage createPartialPaymentMessage() {
        var item = PaymentItemFactory.createPartialPaymentItem();
        return PaymentMessage.fromDomain(item);
    }

    public static PaymentMessage createTotalPaymentMessage() {
        var item = PaymentItemFactory.createTotalPaymentItem();
        return PaymentMessage.fromDomain(item);
    }

    public static PaymentMessage createSurplusPaymentMessage() {
        var item = PaymentItemFactory.createSurplusPaymentItem();
        return PaymentMessage.fromDomain(item);
    }
}
