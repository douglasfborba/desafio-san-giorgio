package br.com.uol.compass.desafio.san_giorgio.infrastructure.config;

import br.com.uol.compass.desafio.san_giorgio.application.domain.service.PaymentMessageSenderService;
import br.com.uol.compass.desafio.san_giorgio.application.domain.service.PaymentService;
import br.com.uol.compass.desafio.san_giorgio.application.port.input.SendPaymentMessageUseCase;
import br.com.uol.compass.desafio.san_giorgio.application.port.output.InvoiceDataAdapter;
import br.com.uol.compass.desafio.san_giorgio.application.port.output.PaymentDataAdapter;
import br.com.uol.compass.desafio.san_giorgio.application.port.output.SellerDataAdapter;
import br.com.uol.compass.desafio.san_giorgio.application.port.output.SendMessageAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class DomainConfig {
    @Bean
    public PaymentService paymentService(final SellerDataAdapter sellerDataAdapter,
                                         final InvoiceDataAdapter invoiceDataAdapter,
                                         final PaymentDataAdapter paymentDataAdapter,
                                         final SendPaymentMessageUseCase sendPaymentMessageUseCase) {
        return new PaymentService(sellerDataAdapter, invoiceDataAdapter, paymentDataAdapter, sendPaymentMessageUseCase);
    }

    @Bean
    public PaymentMessageSenderService paymentMessageSenderService(final Map<String, SendMessageAdapter> messageAdapters) {
        return new PaymentMessageSenderService(messageAdapters);
    }
}
