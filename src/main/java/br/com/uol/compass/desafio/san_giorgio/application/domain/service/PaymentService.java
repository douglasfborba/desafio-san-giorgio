package br.com.uol.compass.desafio.san_giorgio.application.domain.service;

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
        var seller = sellerDataAdapter.findSellerById(payment.getSeller().getId());
        var updatedItems = payment.getPaymentItems().stream()
                .map(paymentItem -> processPaymentItem(seller, paymentItem))
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
        var invoice = invoiceDataAdapter.findByInvoiceId(paymentItem.getInvoice().getId());
        var status = getPaymentStatus(invoice.getTotal(), paymentItem.getPaymentValue());

        var updatedItem = paymentItem.toBuilder()
                .invoice(invoice)
                .seller(seller)
                .build();

        sendPaymentMessageUseCase.send(status, updatedItem);

        updatedItem = updatedItem.toBuilder()
                .paymentStatus(status)
                .build();

        return updatedItem;
    }

    private PaymentStatus getPaymentStatus(final BigDecimal invoicePrice, final BigDecimal paymentValue) {
        return switch(invoicePrice.compareTo(paymentValue)) {
            case 1 -> PARTIAL;
            case -1 -> SURPLUS;
            default -> TOTAL;
        };
    }
}
