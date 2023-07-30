package com.bullish.interview.tinli.controller.cart;

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
public class UpdateCartCommand {

    @NotNull
    @Schema(name = "customerId", required = true)
    private Long customerId;

    @NotNull
    @Schema(name = "productId", required = true)
    private Long productId;

    @NotNull
    @Positive
    @Schema(name = "quantity", description = "Number of products added/removed", example = "2", required = true)
    private Long quantity;

}
