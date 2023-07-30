package com.bullish.interview.tinli.repository.discount;

import com.bullish.interview.tinli.model.discount.AppliedProductDiscount;
import com.bullish.interview.tinli.model.discount.Discount;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public interface AppliedProductDiscountRepository {
    Optional<AppliedProductDiscount> findById(@NotNull Long productId);

    Optional<Discount> findAppliedDiscountByProduct(@NotNull Long productId);

    AppliedProductDiscount applyProductDiscount(@NotNull Long productId, @NotNull Long discountId);

    void revokeProductDiscount(@NotNull Long productId);

}
