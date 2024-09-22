package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.input.rest.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
@Schema(description = "Error item response details.")
public record ErrorItemResponse(
        @Schema(description = "Field name", example = "sellerId")
        String field,

        @Schema(description = "Error message", example = "The field sellerId is required.")
        String message
) {
}
