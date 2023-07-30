package com.bullish.interview.tinli.model.product;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.AUTO;

@Serdeable
@Entity
@Table(name = "Product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long productId;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "price")
    private Float price;

    @PositiveOrZero
    @Column(name = "inventory")
    private Long inventory;

}
