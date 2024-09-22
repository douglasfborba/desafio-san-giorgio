package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.database;

import br.com.uol.compass.desafio.san_giorgio.application.domain.exception.InvoiceNotFoundException;
import br.com.uol.compass.desafio.san_giorgio.application.domain.model.Invoice;
import br.com.uol.compass.desafio.san_giorgio.application.port.output.InvoiceDataAdapter;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.database.entity.InvoiceEntity;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.database.respository.InvoiceJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
@RequiredArgsConstructor
public final class InvoiceJpaAdapter implements InvoiceDataAdapter {
    private final InvoiceJpaRepository invoiceJpaRepository;

    public Invoice findByInvoiceId(final BigInteger invoiceId) {
        return invoiceJpaRepository.findById(invoiceId)
                .map(InvoiceEntity::toDomain)
                .orElseThrow(InvoiceNotFoundException::new);
    }
}
