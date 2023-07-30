package com.bullish.interview.tinli.model.discount;

import com.bullish.interview.tinli.model.cart.CartItem;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Serdeable
@Entity
@DiscriminatorValue(ComboDiscount.DISCRIMINATOR_VALUE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ComboDiscount extends Discount {

    public static final String DISCRIMINATOR_VALUE = "COMBO";

    private Integer comboSize;

    private Float discount;

    @Override
    public Float getDiscountedPrice(CartItem cartItem) {
        Long purchaseQty = cartItem.getQuantity();
        Float productPrice = cartItem.getProduct().getPrice();
        Long ineligibleQty = purchaseQty % comboSize;
        Float totalPrice = ineligibleQty * productPrice
                + (purchaseQty - ineligibleQty) * productPrice * discount;
        return totalPrice;
    }

    @Override
    public boolean isEligible(CartItem cartItem) {
        Long purchaseQty = cartItem.getQuantity();
        return comboSize <= purchaseQty;
    }
}
