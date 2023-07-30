package com.bullish.interview.tinli.controller.cart;

import com.bullish.interview.tinli.model.cart.CartItem;
import com.bullish.interview.tinli.model.cart.Receipt;
import com.bullish.interview.tinli.model.discount.Discount;
import com.bullish.interview.tinli.repository.cart.CartRepository;
import com.bullish.interview.tinli.repository.discount.AppliedProductDiscountRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExecuteOn(TaskExecutors.IO)
@Controller("/cart")
@Tag(name = "Cart API")
public class CartController {

    private final CartRepository cartRepository;
    private final AppliedProductDiscountRepository appliedProductDiscountRepository;

    public CartController(CartRepository cartRepository, AppliedProductDiscountRepository appliedProductDiscountRepository) {
        this.cartRepository = cartRepository;
        this.appliedProductDiscountRepository = appliedProductDiscountRepository;
    }

    @Post("/add")
    @Operation(summary = "Add products into cart", description = "Add products into cart for a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added products into cart for a customer")
    })
    HttpResponse<?> add(@Body @Valid UpdateCartCommand cmd) {
        cartRepository.add(cmd.getCustomerId(), cmd.getProductId(), cmd.getQuantity());
        return HttpResponse.ok();
    }

    @Post("/remove")
    @Operation(summary = "Remove products from cart", description = "Remove products from cart for a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully removed products from cart for a customer")
    })
    HttpResponse<?> remove(@Body @Valid UpdateCartCommand cmd) {
        cartRepository.remove(cmd.getCustomerId(), cmd.getProductId(), cmd.getQuantity());
        return HttpResponse.ok();
    }

    @Get(value = "/list/{customerId}")
    @Operation(summary = "List all items in cart", description = "List all items in cart for a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all items in cart for a customer")
    })
    List<CartItem> list(@PathVariable("customerId") @Parameter(name = "customerId", description = "Customer Id")
                        Long customerId) {
        return cartRepository.findByCustomer(customerId);
    }

    @Get(value = "/receipt/{customerId}")
    @Operation(summary = "Generate a receipt for a cart", description = "Generate a receipt for a cart containing all items purchased, all discount deals applied and the total price")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully generated a receipt for a cart")
    })
    Receipt calculateReceipt(@PathVariable("customerId") @Parameter(name = "customerId", description = "Customer Id")
                             Long customerId) {
        List<CartItem> cartItems = cartRepository.findByCustomer(customerId);
        List<Discount> appliedDiscounts = new ArrayList<>();
        Float totalPrice = 0f;

        for (CartItem item : cartItems) {
            Optional<Discount> appliedDiscount = appliedProductDiscountRepository.findAppliedDiscountByProduct(item.getProductId());
            if (appliedDiscount.isPresent() && appliedDiscount.get().isEligible(item)) {
                appliedDiscounts.add(appliedDiscount.get());
                totalPrice += appliedDiscount.get().getDiscountedPrice(item);
            } else {
                totalPrice += item.getProduct().getPrice() * item.getQuantity();
            }
        }

        Receipt receipt = Receipt.builder()
                .purchases(cartItems)
                .discounts(appliedDiscounts)
                .totalPrice(totalPrice)
                .build();

        return receipt;
    }
}
