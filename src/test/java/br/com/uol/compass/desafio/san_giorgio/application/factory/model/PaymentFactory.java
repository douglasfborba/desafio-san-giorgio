package br.com.uol.compass.desafio.san_giorgio.application.factory.model;

import br.com.uol.compass.desafio.san_giorgio.application.domain.model.Payment;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public class PaymentFactory {

    public static Payment createPayment() {
        return Payment.builder()
                .seller(SellerFactory.createSeller())
                .paymentItems(List.of(PaymentItemFactory.createPaymentItem()))
                .build();
    }

    public static Payment createPartialPayment() {
        return Payment.builder()
                .seller(SellerFactory.createSeller())
                .paymentItems(List.of(PaymentItemFactory.createPartialPaymentItem()))
                .build();
    }

    public static Payment createTotalPayment() {
        return Payment.builder()
                .seller(SellerFactory.createSeller())
                .paymentItems(List.of(PaymentItemFactory.createTotalPaymentItem()))
                .build();
    }

    public static Payment createSurplusPayment() {
        return Payment.builder()
                .seller(SellerFactory.createSeller())
                .paymentItems(List.of(PaymentItemFactory.createSurplusPaymentItem()))
                .build();
    }
}
