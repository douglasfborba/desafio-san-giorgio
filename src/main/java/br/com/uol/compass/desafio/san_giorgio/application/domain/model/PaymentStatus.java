package br.com.uol.compass.desafio.san_giorgio.application.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum PaymentStatus {
    TOTAL("Total"),
    PARTIAL("Parcial"),
    SURPLUS("Excedente");

    private final String value;

    public static PaymentStatus fromValue(final String value) {
        return Arrays.stream(values())
                .filter(status -> status.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }
}
