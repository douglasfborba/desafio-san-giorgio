package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.input.rest.response;

import br.com.uol.compass.desafio.san_giorgio.application.domain.model.Payment;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigInteger;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
@Schema(description = "Payment response details.")
public record PaymentResponse(
        @Schema(description = "Seller Id", example = "123")
        BigInteger sellerId,

        @Schema(description = "List with payment items")
        List<PaymentItemResponse> paymentItems
) {
    public static PaymentResponse fromDomain(final Payment domain) {
        return new PaymentResponse(
                domain.getSeller().getId(),
                domain.getPaymentItems().stream().map(PaymentItemResponse::fromDomain).toList()
        );
    }
}
