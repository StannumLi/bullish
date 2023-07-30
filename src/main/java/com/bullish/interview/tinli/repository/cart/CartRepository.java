package com.bullish.interview.tinli.repository.cart;

import com.bullish.interview.tinli.model.cart.CartItem;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

public interface CartRepository {
    List<CartItem> findByCustomer(@NotNull Long customerId);

    Optional<CartItem> findByKey(@NotNull Long customerId, @NotNull Long productId);

    void add(@NotNull Long customerId, @NotNull Long productId, @NotNull Long quantity);

    void remove(@NotNull Long customerId, @NotNull Long productId, @NotNull Long quantity);

    void deleteByKey(@NotNull Long customerId, @NotNull Long productId);

}
