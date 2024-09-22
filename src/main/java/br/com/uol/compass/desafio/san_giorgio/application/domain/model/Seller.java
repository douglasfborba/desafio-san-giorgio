package br.com.uol.compass.desafio.san_giorgio.application.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Builder(toBuilder = true)
@RequiredArgsConstructor(access = PRIVATE)
public class Seller {
    private final BigInteger id;
    private final String name;
}
