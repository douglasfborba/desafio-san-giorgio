package br.com.uol.compass.desafio.san_giorgio.application.factory;

import br.com.uol.compass.desafio.san_giorgio.application.domain.model.Seller;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public class SellerFactory {
    public static Seller createSeller() {
        return Seller.builder()
                .id(BigInteger.valueOf(1L))
                .name("Francisca Luna Fernandes")
                .build();
    }
}
