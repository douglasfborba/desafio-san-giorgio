package br.com.uol.compass.desafio.san_giorgio.application.factory.model;

import br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentItem;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

import static br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentStatus.PARTIAL;
import static br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentStatus.SURPLUS;
import static br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentStatus.TOTAL;
import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public class PaymentItemFactory {

    public static PaymentItem createPaymentItem() {
        return PaymentItem.builder()
                .id(BigInteger.valueOf(1L))
                .invoice(InvoiceFactory.createInvoice())
                .seller(SellerFactory.createSeller())
                .paymentValue(BigDecimal.valueOf(1000.00))
                .build();
    }

    public static PaymentItem createPartialPaymentItem() {
        return PaymentItem.builder()
                .id(BigInteger.valueOf(1L))
                .invoice(InvoiceFactory.createInvoice())
                .seller(SellerFactory.createSeller())
                .paymentStatus(PARTIAL)
                .paymentValue(BigDecimal.valueOf(1000.00))
                .build();
    }

    public static PaymentItem createTotalPaymentItem() {
        return PaymentItem.builder()
                .id(BigInteger.valueOf(1L))
                .invoice(InvoiceFactory.createInvoice())
                .seller(SellerFactory.createSeller())
                .paymentStatus(TOTAL)
                .paymentValue(BigDecimal.valueOf(2000.00))
                .build();
    }

    public static PaymentItem createSurplusPaymentItem() {
        return PaymentItem.builder()
                .id(BigInteger.valueOf(1L))
                .invoice(InvoiceFactory.createInvoice())
                .seller(SellerFactory.createSeller())
                .paymentStatus(SURPLUS)
                .paymentValue(BigDecimal.valueOf(3000.00))
                .build();
    }
}
