package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.input.rest.request;

import br.com.uol.compass.desafio.san_giorgio.application.domain.model.Invoice;
import br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentItem;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.math.BigInteger;

@Schema(description = "Payment item request details.")
public record PaymentItemRequest(
        @NotNull(message = "The field invoiceId is required.")
        @Positive(message = "The field invoiceId must be greater than zero.")
        @Schema(description = "Invoice Id", example = "123")
        BigInteger invoiceId,

        @Valid
        @NotNull(message = "The field paymentValue is required.")
        @Positive(message = "The field paymentValue must be greater than zero.")
        @Schema(description = "Payment amount", example = "250.89")
        BigDecimal paymentValue
) {
    public PaymentItem toDomain() {
        return PaymentItem.builder()
                .invoice(Invoice.builder().id(invoiceId).build())
                .paymentValue(paymentValue)
                .build();
    }
}