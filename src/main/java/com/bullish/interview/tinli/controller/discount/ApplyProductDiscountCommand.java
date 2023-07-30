package com.bullish.interview.tinli.controller.discount;

import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Serdeable
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ApplyProductDiscountCommand {

    @NotNull
    @Schema(name = "productId", required = true)
    private Long productId;

    @NotNull
    @Schema(name = "discountId", required = true)
    private Long discountId;

}
