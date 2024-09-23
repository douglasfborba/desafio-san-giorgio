package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.database;

import br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentItem;
import br.com.uol.compass.desafio.san_giorgio.application.port.output.PaymentDataAdapter;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.database.entity.PaymentEntity;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.database.respository.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class PaymentJpaAdapter implements PaymentDataAdapter {

    private final PaymentJpaRepository paymentJpaRepository;

    public PaymentItem save(final PaymentItem item) {
        return paymentJpaRepository
                .save(PaymentEntity.fromDomain(item))
                .toDomain();
    }
}
