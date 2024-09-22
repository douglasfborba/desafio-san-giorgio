package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.input.rest.response;

import br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentItem;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.math.BigInteger;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
@Schema(description = "Payment item response details.")
public record PaymentItemResponse(
        @Schema(description = "Invoice Id", example = "123")
        BigInteger invoiceId,

        @Schema(description = "Payment amount", example = "250.89")
        BigDecimal paymentValue,

        @Schema(description = "Payment status", example = "[TOTAL, PARTIAL, SURPLUS]")
        String paymentStatus
) {
    public static PaymentItemResponse fromDomain(final PaymentItem domain) {
        return new PaymentItemResponse(
                domain.getId(),
                domain.getPaymentValue(),
                domain.getPaymentStatus().name()
        );
    }
}