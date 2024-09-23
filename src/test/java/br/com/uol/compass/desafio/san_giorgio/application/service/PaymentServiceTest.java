package br.com.uol.compass.desafio.san_giorgio.application.service;

import br.com.uol.compass.desafio.san_giorgio.application.domain.exception.InvoiceNotFoundException;
import br.com.uol.compass.desafio.san_giorgio.application.domain.exception.SellerNotFoundException;
import br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentItem;
import br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentStatus;
import br.com.uol.compass.desafio.san_giorgio.application.domain.service.PaymentService;
import br.com.uol.compass.desafio.san_giorgio.application.factory.model.InvoiceFactory;
import br.com.uol.compass.desafio.san_giorgio.application.factory.model.PaymentFactory;
import br.com.uol.compass.desafio.san_giorgio.application.factory.model.PaymentItemFactory;
import br.com.uol.compass.desafio.san_giorgio.application.factory.model.SellerFactory;
import br.com.uol.compass.desafio.san_giorgio.application.port.input.SendPaymentMessageUseCase;
import br.com.uol.compass.desafio.san_giorgio.application.port.output.InvoiceDataAdapter;
import br.com.uol.compass.desafio.san_giorgio.application.port.output.PaymentDataAdapter;
import br.com.uol.compass.desafio.san_giorgio.application.port.output.SellerDataAdapter;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.exception.SendMessageErrorException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigInteger;

import static br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentStatus.PARTIAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;

@ExtendWith(SpringExtension.class)
class PaymentServiceTest {

    @Mock
    private SellerDataAdapter sellerDataAdapter;

    @Mock
    private InvoiceDataAdapter invoiceDataAdapter;

    @Mock
    private PaymentDataAdapter paymentDataAdapter;

    @Mock
    private SendPaymentMessageUseCase sendPaymentMessageUseCase;

    @InjectMocks
    private PaymentService paymentService;

    @ParameterizedTest
    @EnumSource(PaymentStatus.class)
    @DisplayName("Should confirm payments successfully.")
    void shouldConfirmPaymentsSuccessfully(final PaymentStatus status) {
        var payment = switch(status) {
            case PARTIAL -> PaymentFactory.createPartialPayment();
            case TOTAL -> PaymentFactory.createTotalPayment();
            default -> PaymentFactory.createSurplusPayment();
        };

        var seller = SellerFactory.createSeller();
        var invoice = InvoiceFactory.createInvoice();

        when(sellerDataAdapter.findSellerById(any(BigInteger.class)))
                .thenReturn(seller);

        when(invoiceDataAdapter.findByInvoiceId(any(BigInteger.class)))
                .thenReturn(invoice);

        doNothing()
                .when(sendPaymentMessageUseCase)
                .send(any(PaymentStatus.class), any(PaymentItem.class));

        var result = paymentService.confirm(payment);

        verify(sellerDataAdapter, times(1))
                .findSellerById(any(BigInteger.class));
        verify(invoiceDataAdapter, times(1))
                .findByInvoiceId(any(BigInteger.class));
        verify(sendPaymentMessageUseCase, times(1))
                .send(any(PaymentStatus.class), any(PaymentItem.class));

        assertThat(result).isNotNull();
        assertThat(result.getSeller().getId()).isEqualTo(seller.getId());
        assertThat(result.getPaymentItems().get(0).getInvoice().getId()).isEqualTo(invoice.getId());
        assertThat(result.getPaymentItems().get(0).getPaymentStatus()).isEqualTo(status);
    }

    @Test
    @DisplayName("Should throw SellerNotFoundException when seller not found.")
    void shouldThrowSellerNotFoundExceptionWhenSellerNotFound() {
        var payment = PaymentFactory.createPartialPayment();

        when(sellerDataAdapter.findSellerById(any(BigInteger.class)))
                .thenThrow(new SellerNotFoundException());

        var exception = assertThrows(
                SellerNotFoundException.class,
                () -> paymentService.confirm(payment)
        );

        verify(sellerDataAdapter, times(1))
                .findSellerById(any(BigInteger.class));
        verify(invoiceDataAdapter, never())
                .findByInvoiceId(any(BigInteger.class));
        verify(sendPaymentMessageUseCase, never())
                .send(any(PaymentStatus.class), any(PaymentItem.class));

        assertThat(exception).isNotNull();
        assertThat(exception.getStatus()).isEqualTo(PRECONDITION_FAILED);
        assertThat(exception.getMessage()).contains("Seller not found.");
    }

    @Test
    @DisplayName("Should throw InvoiceNotFoundException when invoice not found.")
    void shouldThrowInvoiceNotFoundExceptionWhenInvoiceNotFound() {
        var payment = PaymentFactory.createPartialPayment();
        var seller = SellerFactory.createSeller();

        when(sellerDataAdapter.findSellerById(any(BigInteger.class)))
                .thenReturn(seller);

        when(invoiceDataAdapter.findByInvoiceId(any(BigInteger.class)))
                .thenThrow(new InvoiceNotFoundException());

        var exception = assertThrows(
                InvoiceNotFoundException.class,
                () -> paymentService.confirm(payment)
        );

        verify(sellerDataAdapter, times(1))
                .findSellerById(any(BigInteger.class));
        verify(invoiceDataAdapter, times(1))
                .findByInvoiceId(any(BigInteger.class));
        verify(sendPaymentMessageUseCase, never())
                .send(any(PaymentStatus.class), any(PaymentItem.class));

        assertThat(exception).isNotNull();
        assertThat(exception.getStatus()).isEqualTo(PRECONDITION_FAILED);
        assertThat(exception.getMessage()).contains("Invoice not found.");
    }

    @Test
    @DisplayName("Should throw SendMessageErrorException when error to send message.")
    void shouldThrowSendMessageErrorExceptionWhenErrorToSendMessage() {
        var payment = PaymentFactory.createPartialPayment();
        var seller = SellerFactory.createSeller();
        var invoice = InvoiceFactory.createInvoice();

        when(sellerDataAdapter.findSellerById(any(BigInteger.class)))
                .thenReturn(seller);

        when(invoiceDataAdapter.findByInvoiceId(any(BigInteger.class)))
                .thenReturn(invoice);

        doThrow(new SendMessageErrorException())
                .when(sendPaymentMessageUseCase)
                .send(any(PaymentStatus.class), any(PaymentItem.class));

        var exception = assertThrows(
                SendMessageErrorException.class,
                () -> paymentService.confirm(payment)
        );

        verify(sellerDataAdapter, times(1))
                .findSellerById(any(BigInteger.class));
        verify(invoiceDataAdapter, times(1))
                .findByInvoiceId(any(BigInteger.class));
        verify(sendPaymentMessageUseCase, times(1))
                .send(any(PaymentStatus.class), any(PaymentItem.class));

        assertThat(exception).isNotNull();
        assertThat(exception.getStatus()).isEqualTo(PRECONDITION_FAILED);
        assertThat(exception.getMessage()).contains("Error to send message to the queue.");
    }

    @ParameterizedTest
    @EnumSource(PaymentStatus.class)
    @DisplayName("Should update payments status successfully.")
    void shouldUpdatePaymentsStatusSuccessfully(final PaymentStatus status) {
        var payment = switch(status) {
            case PARTIAL -> PaymentFactory.createPartialPayment();
            case TOTAL -> PaymentFactory.createTotalPayment();
            default -> PaymentFactory.createSurplusPayment();
        };

        var item = switch(status) {
            case PARTIAL -> PaymentItemFactory.createPartialPaymentItem();
            case TOTAL -> PaymentItemFactory.createTotalPaymentItem();
            default -> PaymentItemFactory.createSurplusPaymentItem();
        };

        when(paymentDataAdapter.save(any(PaymentItem.class)))
                .thenReturn(item);

        var result = paymentService.update(status, payment);

        verify(paymentDataAdapter, times(1))
                .save(any(PaymentItem.class));

        assertThat(result).isNotNull();
        assertThat(result.getSeller().getId()).isEqualTo(item.getSeller().getId());
        assertThat(result.getPaymentItems().get(0).getInvoice().getId()).isEqualTo(item.getInvoice().getId());
        assertThat(result.getPaymentItems().get(0).getPaymentStatus()).isEqualTo(item.getPaymentStatus());
    }

    @Test
    @DisplayName("Should propagate exception when catch unexpected error.")
    void shouldPropagateExceptionWhenCatchUnexpectedError() {
        var payment = PaymentFactory.createPartialPayment();

        when(paymentDataAdapter.save(any(PaymentItem.class)))
                .thenThrow(new NullPointerException());

        var exception = assertThrows(
                NullPointerException.class,
                () -> paymentService.update(PARTIAL, payment)
        );

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNull();
    }
}
