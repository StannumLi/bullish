package com.bullish.interview.tinli.model.cart;

import com.bullish.interview.tinli.model.discount.Discount;
import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Serdeable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Receipt {

    private List<CartItem> purchases;

    private List<Discount> discounts;

    private Float totalPrice;
}
