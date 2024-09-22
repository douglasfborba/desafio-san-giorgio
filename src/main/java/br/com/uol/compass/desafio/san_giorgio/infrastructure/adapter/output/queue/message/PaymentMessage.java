package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.queue.message;

import br.com.uol.compass.desafio.san_giorgio.application.domain.model.Invoice;
import br.com.uol.compass.desafio.san_giorgio.application.domain.model.Payment;
import br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentItem;
import br.com.uol.compass.desafio.san_giorgio.application.domain.model.Seller;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
public record PaymentMessage(
        BigInteger invoiceId,
        BigInteger sellerId,
        BigDecimal paymentValue
) {
    public Payment toDomain() {
        var seller = Seller.builder().id(sellerId).build();
        return Payment.builder()
                .seller(seller)
                .paymentItems(List.of(
                    PaymentItem.builder()
                            .invoice(Invoice.builder().id(invoiceId).build())
                            .seller(seller)
                            .paymentValue(paymentValue)
                            .build()
                ))
                .build();
    }
    
    public static PaymentMessage fromDomain(final PaymentItem domain) {
        return new PaymentMessage(
                domain.getInvoice().getId(),
                domain.getSeller().getId(),
                domain.getPaymentValue()
        );
    }
}
