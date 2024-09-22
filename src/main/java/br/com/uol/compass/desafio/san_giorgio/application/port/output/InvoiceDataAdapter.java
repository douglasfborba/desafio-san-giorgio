package br.com.uol.compass.desafio.san_giorgio.application.port.output;

import br.com.uol.compass.desafio.san_giorgio.application.domain.model.Invoice;

import java.math.BigInteger;

public interface InvoiceDataAdapter {
    Invoice findByInvoiceId(final BigInteger invoiceId);
}
