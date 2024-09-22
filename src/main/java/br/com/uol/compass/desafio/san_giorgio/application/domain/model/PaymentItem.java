package br.com.uol.compass.desafio.san_giorgio.application.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Builder(toBuilder = true)
@RequiredArgsConstructor(access = PRIVATE)
public class PaymentItem {
    private final BigInteger id;
    private final Invoice invoice;
    private final Seller seller;
    private final BigDecimal paymentValue;
    private final PaymentStatus paymentStatus;
}
