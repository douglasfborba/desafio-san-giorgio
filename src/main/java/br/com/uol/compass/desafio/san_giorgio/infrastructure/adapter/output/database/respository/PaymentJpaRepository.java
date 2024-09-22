package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.database.respository;

import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.database.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, BigInteger> {
}
