package com.bullish.interview.tinli.controller.customer;

import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Serdeable
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CreateCustomerCommand {

    @NotBlank
    @Schema(name = "username", example = "username", required = true)
    private String username;

}
