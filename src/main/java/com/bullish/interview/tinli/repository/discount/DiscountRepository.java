package com.bullish.interview.tinli.repository.discount;

import com.bullish.interview.tinli.model.discount.Discount;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

public interface DiscountRepository {
    Optional<Discount> findById(@NotNull Long discountId);

    List<Discount> findByType(String type);

    List<Discount> findAll();

    Discount createComboDiscount(@NotNull Integer comboSize, @NotNull Float discount);

    void deleteDiscountById(@NotNull Long discountId);

}
