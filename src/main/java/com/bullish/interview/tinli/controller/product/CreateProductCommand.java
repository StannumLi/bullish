package com.bullish.interview.tinli.controller.product;

import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Serdeable
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CreateProductCommand {

    @NotBlank
    @Schema(name = "name", example = "product name", required = true)
    private String name;

    @NotNull
    @Positive
    @Schema(name = "price", example = "10.5", required = true)
    private Float price;

    @PositiveOrZero
    @Schema(name = "inventory", example = "1000")
    private Long inventory;
}
