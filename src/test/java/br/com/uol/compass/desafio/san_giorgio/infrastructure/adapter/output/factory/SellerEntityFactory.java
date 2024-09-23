package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.factory;

import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.database.entity.SellerEntity;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public class SellerEntityFactory {

    public static SellerEntity createSellerEntity() {
        return SellerEntity.builder()
                .id(BigInteger.valueOf(3))
                .name("Francisca Luna Fernandes")
                .build();
    }
}
