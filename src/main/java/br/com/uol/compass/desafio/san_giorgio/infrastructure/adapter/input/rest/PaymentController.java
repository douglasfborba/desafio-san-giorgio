package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.input.rest;

import br.com.uol.compass.desafio.san_giorgio.application.port.input.ConfirmPaymentUseCase;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.input.rest.annotation.swagger.CustomConfirmPaymentResponses;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.input.rest.request.PaymentRequest;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.input.rest.response.PaymentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Payment", description = "Payment management APIs")
public class PaymentController {

    private final ConfirmPaymentUseCase confirmPaymentUseCase;

    @PutMapping(path = "/v1/payment")
    @Operation(
            summary = "Confirm payments",
            description = "Update a Payment status based in input object details. The response is Payment object with the updated status.",
            tags = {"Payment"}
    )
    @CustomConfirmPaymentResponses
    public ResponseEntity<PaymentResponse> confirmPayment(@Valid @RequestBody final PaymentRequest request) {
        var domain = confirmPaymentUseCase.confirm(request.toDomain());
        return ResponseEntity.ok(PaymentResponse.fromDomain(domain));
    }
}
