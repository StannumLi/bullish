package com.bullish.interview.tinli.controller.discount;

import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Serdeable
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CreateComboDiscountCommand {

    @NotNull
    @Positive
    @Schema(name = "comboSize", description = "Fixed number of products the discount applied", example = "2", required = true)
    private Integer comboSize;

    @NotNull
    @Positive
    @Schema(name = "discount", example = "0.75", required = true)
    private Float discount;
}
