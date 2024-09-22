package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.input.rest.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Timestamp;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
@Schema(description = "Error response details.")
public record ErrorResponse(
        @Schema(description = "Timestamp", example = "1727019242745")
        Timestamp timestamp,

        @Schema(description = "Http status code", example = "400")
        Integer status,

        @Schema(description = "Endpoint URI", example = "/api/v1/payment")
        String path,

        @Schema(description = "List with errors")
        List<ErrorItemResponse> errors
) {
}
