package br.com.uol.compass.desafio.san_giorgio.application.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Builder(toBuilder = true)
@RequiredArgsConstructor(access = PRIVATE)
public class Payment {
    private final Seller seller;
    private final List<PaymentItem> paymentItems;
}
