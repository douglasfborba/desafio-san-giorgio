package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.database.respository;

import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.database.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface InvoiceJpaRepository extends JpaRepository<InvoiceEntity, BigInteger> {
}
