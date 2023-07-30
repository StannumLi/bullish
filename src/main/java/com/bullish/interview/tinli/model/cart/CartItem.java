package com.bullish.interview.tinli.model.cart;

import com.bullish.interview.tinli.model.customer.Customer;
import com.bullish.interview.tinli.model.product.Product;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Serdeable
@Entity
@Table(name = "CartItem")
@IdClass(CartItemKey.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    private Long customerId;

    @Id
    private Long productId;

    private Long quantity;

    @ManyToOne
    @MapsId("customerId")
    @JoinColumn(name = "customerId")
    private Customer customer;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "productId")
    private Product product;


}

