package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.input.rest.annotation.swagger;

import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.input.rest.response.ErrorResponse;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.input.rest.response.PaymentResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ApiResponses
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface CustomConfirmPaymentResponses {

    ApiResponse[] value() default {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully processed.",
                    content = {@Content(schema = @Schema(implementation = PaymentResponse.class), mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request.",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "412",
                    description = "Precondition failed.",
                    content = {@Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                      "timestamp": "2024-09-22T16:26:42.071Z",
                                      "status": 412,
                                      "path": "/api/v1/payment",
                                      "errors": [
                                        {
                                          "field": "sellerId",
                                          "message": "Seller id not found."
                                        }
                                      ]
                                    }
                                    """),
                            mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error.",
                    content = {@Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                      "timestamp": "2024-09-22T16:26:42.071Z",
                                      "status": 500,
                                      "path": "/api/v1/payment",
                                      "errors": [
                                        {
                                          "message": "Error to process request."
                                        }
                                      ]
                                    }
                                    """),
                            mediaType = "application/json")}
            )
    };
}
