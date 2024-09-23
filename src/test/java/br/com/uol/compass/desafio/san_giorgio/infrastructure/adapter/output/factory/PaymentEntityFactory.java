package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.factory;

import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.database.entity.PaymentEntity;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

import static br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentStatus.PARTIAL;
import static br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentStatus.SURPLUS;
import static br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentStatus.TOTAL;
import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public class PaymentEntityFactory {

    public static PaymentEntity createPaymentEntity() {
        return PaymentEntity.builder()
                .id(BigInteger.valueOf(1))
                .amount(BigDecimal.valueOf(1000.00))
                .invoice(InvoiceEntityFactory.createInvoiceEntity())
                .seller(SellerEntityFactory.createSellerEntity())
                .build();
    }

    public static PaymentEntity createPartialPaymentEntity() {
        return PaymentEntity.builder()
                .id(BigInteger.valueOf(1))
                .status(PARTIAL.getValue())
                .amount(BigDecimal.valueOf(1000.00))
                .invoice(InvoiceEntityFactory.createInvoiceEntity())
                .seller(SellerEntityFactory.createSellerEntity())
                .build();
    }

    public static PaymentEntity createTotalPaymentEntity() {
        return PaymentEntity.builder()
                .id(BigInteger.valueOf(1))
                .status(TOTAL.getValue())
                .amount(BigDecimal.valueOf(2000.00))
                .invoice(InvoiceEntityFactory.createInvoiceEntity())
                .seller(SellerEntityFactory.createSellerEntity())
                .build();
    }

    public static PaymentEntity createSurplusPaymentEntity() {
        return PaymentEntity.builder()
                .id(BigInteger.valueOf(1))
                .status(SURPLUS.getValue())
                .amount(BigDecimal.valueOf(3000.00))
                .invoice(InvoiceEntityFactory.createInvoiceEntity())
                .seller(SellerEntityFactory.createSellerEntity())
                .build();
    }
}
