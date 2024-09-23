package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.factory;

import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.database.entity.InvoiceEntity;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public class InvoiceEntityFactory {

    public static InvoiceEntity createInvoiceEntity() {
        return InvoiceEntity.builder()
                .id(BigInteger.valueOf(1))
                .total(BigDecimal.valueOf(2000.00))
                .description("Yasmin e Igor Gráfica Ltda")
                .build();
    }
}
