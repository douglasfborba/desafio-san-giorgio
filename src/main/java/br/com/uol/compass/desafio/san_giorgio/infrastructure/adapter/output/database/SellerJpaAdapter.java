package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.database;

import br.com.uol.compass.desafio.san_giorgio.application.domain.exception.SellerNotFoundException;
import br.com.uol.compass.desafio.san_giorgio.application.domain.model.Seller;
import br.com.uol.compass.desafio.san_giorgio.application.port.output.SellerDataAdapter;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.database.entity.SellerEntity;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.database.respository.SellerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
@RequiredArgsConstructor
public final class SellerJpaAdapter implements SellerDataAdapter {
    private final SellerJpaRepository sellerJpaRepository;

    public Seller findSellerById(final BigInteger sellerId) {
        return sellerJpaRepository.findById(sellerId)
                .map(SellerEntity::toDomain)
                .orElseThrow(SellerNotFoundException::new);
    }
}
