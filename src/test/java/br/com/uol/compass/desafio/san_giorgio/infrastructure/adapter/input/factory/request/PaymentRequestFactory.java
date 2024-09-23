package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.input.factory.request;

import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.input.rest.request.PaymentRequest;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public class PaymentRequestFactory {

    public static PaymentRequest createPartialPaymentRequest() {
        return new PaymentRequest(
                BigInteger.valueOf(3L),
                List.of(PaymentItemRequestFactory.createPartialPaymentItemRequest())
        );
    }

    public static PaymentRequest creatTotalPaymentRequest() {
        return new PaymentRequest(
                BigInteger.valueOf(3L),
                List.of(PaymentItemRequestFactory.createTotalPaymentItemRequest())
        );
    }

    public static PaymentRequest creatSurplusPaymentRequest() {
        return new PaymentRequest(
                BigInteger.valueOf(3L),
                List.of(PaymentItemRequestFactory.createSurplusPaymentItemRequest())
        );
    }

    public static PaymentRequest createPartialPaymentRequestWithNullValue() {
        return new PaymentRequest(
                BigInteger.valueOf(3),
                List.of(PaymentItemRequestFactory.createPartialPaymentItemRequestWithNullValue())
        );
    }

    public static PaymentRequest createPartialPaymentRequestWithNullSeller() {
        return new PaymentRequest(
                null,
                List.of(PaymentItemRequestFactory.createPartialPaymentItemRequest())
        );
    }
}
