package br.com.uol.compass.desafio.san_giorgio.infrastructure.config;

import br.com.uol.compass.desafio.san_giorgio.application.domain.service.PaymentMessageSenderService;
import br.com.uol.compass.desafio.san_giorgio.application.domain.service.PaymentService;
import br.com.uol.compass.desafio.san_giorgio.application.port.input.SendPaymentMessageUseCase;
import br.com.uol.compass.desafio.san_giorgio.application.port.input.UpdatePaymentStatusUseCase;
import br.com.uol.compass.desafio.san_giorgio.application.port.output.InvoiceDataAdapter;
import br.com.uol.compass.desafio.san_giorgio.application.port.output.PaymentDataAdapter;
import br.com.uol.compass.desafio.san_giorgio.application.port.output.SellerDataAdapter;
import br.com.uol.compass.desafio.san_giorgio.application.port.output.SendMessageAdapter;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.queue.PartialMessageSenderAdapter;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.queue.SurplusMessageSenderAdapter;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.queue.TotalMessageSenderAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentStatus.PARTIAL;
import static br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentStatus.SURPLUS;
import static br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentStatus.TOTAL;

@Configuration
public class DomainConfig {

    @Bean
    public UpdatePaymentStatusUseCase paymentService(final SellerDataAdapter sellerDataAdapter,
                                                     final InvoiceDataAdapter invoiceDataAdapter,
                                                     final PaymentDataAdapter paymentDataAdapter,
                                                     final SendPaymentMessageUseCase sendPaymentMessageUseCase) {
        return new PaymentService(sellerDataAdapter, invoiceDataAdapter, paymentDataAdapter, sendPaymentMessageUseCase);
    }

    @Bean
    public SendPaymentMessageUseCase paymentMessageSenderService(final Map<String, SendMessageAdapter> messageAdapters) {
        return new PaymentMessageSenderService(messageAdapters);
    }

    @Bean
    public Map<String, SendMessageAdapter> sendMessageAdapters(final PartialMessageSenderAdapter partialMessageSenderAdapter,
                                                               final TotalMessageSenderAdapter totalMessageSenderAdapter,
                                                               final SurplusMessageSenderAdapter surplusMessageSenderAdapter) {
        return Map.ofEntries(
                Map.entry(PARTIAL.name().toLowerCase(), partialMessageSenderAdapter),
                Map.entry(TOTAL.name().toLowerCase(), partialMessageSenderAdapter),
                Map.entry(SURPLUS.name().toLowerCase(), partialMessageSenderAdapter)
        );
    }
}
