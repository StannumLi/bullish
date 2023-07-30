package com.bullish.interview.tinli.model.cart;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemKey implements Serializable {

    @Column(name = "customerId")
    private Long customerId;

    @Column(name = "productId")
    private Long productId;

}

