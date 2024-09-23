package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.input.factory.request;

import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.input.rest.request.PaymentItemRequest;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public class PaymentItemRequestFactory {

    public static PaymentItemRequest createPartialPaymentItemRequest() {
        return new PaymentItemRequest(
                BigInteger.valueOf(1),
                BigDecimal.valueOf(1000.00)
        );
    }

    public static PaymentItemRequest createTotalPaymentItemRequest() {
        return new PaymentItemRequest(
                BigInteger.valueOf(1),
                BigDecimal.valueOf(2000.00)
        );
    }

    public static PaymentItemRequest createSurplusPaymentItemRequest() {
        return new PaymentItemRequest(
                BigInteger.valueOf(1),
                BigDecimal.valueOf(3000.00)
        );
    }

    public static PaymentItemRequest createPartialPaymentItemRequestWithNullValue() {
        return new PaymentItemRequest(
                BigInteger.valueOf(1L),
                null
        );
    }
}
