package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.input.rest.request;

import br.com.uol.compass.desafio.san_giorgio.application.domain.model.Payment;
import br.com.uol.compass.desafio.san_giorgio.application.domain.model.Seller;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigInteger;
import java.util.List;

@Schema(description = "Payment request details.")
public record PaymentRequest(
        @NotNull(message = "The field sellerId is required.")
        @Positive(message = "The field sellerId must be greater than zero.")
        @Schema(description = "Seller Id", example = "123")
        BigInteger sellerId,

        @Valid
        @NotEmpty(message = "The field paymentItems must not be empty or null.")
        @Schema(description = "List with payment items")
        List<PaymentItemRequest> paymentItems
) {
    public Payment toDomain() {
        return Payment.builder()
                .seller(Seller.builder().id(sellerId).build())
                .paymentItems(paymentItems.stream().map(PaymentItemRequest::toDomain).toList())
                .build();
    }
}