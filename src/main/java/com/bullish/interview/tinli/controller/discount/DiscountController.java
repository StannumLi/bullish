package com.bullish.interview.tinli.controller.discount;

import com.bullish.interview.tinli.model.discount.AppliedProductDiscount;
import com.bullish.interview.tinli.model.discount.Discount;
import com.bullish.interview.tinli.repository.discount.AppliedProductDiscountRepository;
import com.bullish.interview.tinli.repository.discount.DiscountRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@ExecuteOn(TaskExecutors.IO)
@Controller("/discount")
@Tag(name = "Discount API")
public class DiscountController {

    private final DiscountRepository repository;

    private final AppliedProductDiscountRepository appliedProductDiscountRepository;

    public DiscountController(DiscountRepository repository, AppliedProductDiscountRepository appliedProductDiscountRepository) {
        this.repository = repository;
        this.appliedProductDiscountRepository = appliedProductDiscountRepository;
    }

    @Post("/create/comboDiscount")
    @Operation(summary = "Create a combo discount deal", description = "Create a new combo discount deal. A combo discount applies a discount rate to a fixed number of products purchased. E.g, buy 1 get 1 with 50% off")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created")
    })
    HttpResponse<Discount> createComboDiscount(@Body @Valid CreateComboDiscountCommand cmd) {
        Discount discount = repository.createComboDiscount(cmd.getComboSize(), cmd.getDiscount());

        return HttpResponse.created(discount);
    }

    @Delete("/delete/{id}")
    @Operation(summary = "Delete a discount deal", description = "Delete a discount deal by discountId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted")
    })
    HttpResponse<?> delete(Long id) {
        repository.deleteDiscountById(id);
        return HttpResponse.noContent();
    }

    @Get(value = "/list/{type}")
    @Operation(summary = "List all discount deals of a certain type", description = "List all discount deals of a certain type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all discount deals of a certain type")
    })
    List<Discount> listComboDiscount(@PathVariable("type") @Parameter(name = "type", description = "Discount type", example = "COMBO")
                                     String type) {
        return repository.findByType(type);
    }

    @Get(value = "/listApplied/{productId}")
    @Operation(summary = "List all discount deals applied on a product", description = "List all discount deals applied on a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all discount deals applied on a product"),
            @ApiResponse(responseCode = "404", description = "No discount deals applied on the product")
    })
    Optional<Discount> listAppliedProductDiscount(@PathVariable("productId") @Parameter(name = "productId", description = "Product Id")
                                                  Long productId) {
        return appliedProductDiscountRepository.findAppliedDiscountByProduct(productId);
    }

    @Post("/apply/productDiscount")
    @Operation(summary = "Apply a discount deals onto a product", description = "Apply a discount deals onto a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully applied a discount deals onto a product")
    })
    HttpResponse<AppliedProductDiscount> applyProductDiscount(@Body @Valid ApplyProductDiscountCommand cmd) {
        AppliedProductDiscount appliedProductDiscount = appliedProductDiscountRepository.applyProductDiscount(cmd.getProductId(), cmd.getDiscountId());
        return HttpResponse.created(appliedProductDiscount);
    }

    @Delete("/revoke/productDiscount/{productId}")
    @Operation(summary = "Revoke a discount deals from a product", description = "Revoke a discount deals from a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully revoke a discount deals from a product")
    })
    HttpResponse<?> revokeProductDiscount(@PathVariable("productId") @Parameter(name = "productId", description = "Product Id")
                                          Long productId) {
        appliedProductDiscountRepository.revokeProductDiscount(productId);
        return HttpResponse.noContent();
    }
}
