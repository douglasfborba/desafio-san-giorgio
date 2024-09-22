package br.com.uol.compass.desafio.san_giorgio.application.domain.service;

import br.com.uol.compass.desafio.san_giorgio.application.domain.model.Invoice;
import br.com.uol.compass.desafio.san_giorgio.application.domain.model.Payment;
import br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentItem;
import br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentStatus;
import br.com.uol.compass.desafio.san_giorgio.application.domain.model.Seller;
import br.com.uol.compass.desafio.san_giorgio.application.port.input.ConfirmPaymentUseCase;
import br.com.uol.compass.desafio.san_giorgio.application.port.input.SendPaymentMessageUseCase;
import br.com.uol.compass.desafio.san_giorgio.application.port.input.UpdatePaymentStatusUseCase;
import br.com.uol.compass.desafio.san_giorgio.application.port.output.InvoiceDataAdapter;
import br.com.uol.compass.desafio.san_giorgio.application.port.output.PaymentDataAdapter;
import br.com.uol.compass.desafio.san_giorgio.application.port.output.SellerDataAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentStatus.PARTIAL;
import static br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentStatus.SURPLUS;
import static br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentStatus.TOTAL;

@Transactional
@RequiredArgsConstructor
public class PaymentService implements ConfirmPaymentUseCase, UpdatePaymentStatusUseCase {
    private final SellerDataAdapter sellerDataAdapter;
    private final InvoiceDataAdapter invoiceDataAdapter;
    private final PaymentDataAdapter paymentDataAdapter;
    private final SendPaymentMessageUseCase sendPaymentMessageUseCase;

    @Override
    public Payment confirm(final Payment payment) {
        var updatedItems = payment.getPaymentItems().stream()
                .map(paymentItem -> processPaymentItem(getSeller(payment), paymentItem))
                .toList();

        return payment.toBuilder()
                .paymentItems(updatedItems)
                .build();
    }

    @Override
    public Payment update(final PaymentStatus status, final Payment payment) {
        var paymentItem = payment.getPaymentItems().get(0);
        var savedItem = paymentDataAdapter.save(
                paymentItem.toBuilder()
                        .paymentStatus(status)
                        .build()
        );

        return payment.toBuilder()
                .paymentItems(List.of(savedItem))
                .build();
    }

    private PaymentItem processPaymentItem(final Seller seller, final PaymentItem paymentItem) {
        var paymentStatus = determineStatus(getInvoice(paymentItem).getTotal(), paymentItem.getPaymentValue());
        var updatedItem = paymentItem.toBuilder()
                .invoice(getInvoice(paymentItem))
                .seller(seller)
                .build();

        sendPaymentMessageUseCase.send(paymentStatus, updatedItem);

        updatedItem = updatedItem.toBuilder()
                .paymentStatus(paymentStatus)
                .build();

        return updatedItem;
    }

    private PaymentStatus determineStatus(final BigDecimal invoicePrice, final BigDecimal paymentValue) {
        var comparison = invoicePrice.compareTo(paymentValue);

        if (comparison > 0) {
            return PARTIAL;
        } else if (comparison < 0) {
            return SURPLUS;
        } else {
            return TOTAL;
        }
    }

    private Seller getSeller(final Payment payment) {
        return sellerDataAdapter.findSellerById(payment.getSeller().getId());
    }

    private Invoice getInvoice(final PaymentItem paymentItem) {
        return invoiceDataAdapter.findByInvoiceId(paymentItem.getInvoice().getId());
    }
}
