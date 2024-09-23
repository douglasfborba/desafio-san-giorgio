package br.com.uol.compass.desafio.san_giorgio.application.factory.model;

import br.com.uol.compass.desafio.san_giorgio.application.domain.model.Invoice;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public class InvoiceFactory {
    public static Invoice createInvoice() {
        return Invoice.builder()
                .id(BigInteger.valueOf(1L))
                .total(BigDecimal.valueOf(2000.00))
                .description("Yasmin e Igor Gráfica Ltda")
                .build();
    }
}
