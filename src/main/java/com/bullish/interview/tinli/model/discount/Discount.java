package com.bullish.interview.tinli.model.discount;

import com.bullish.interview.tinli.model.cart.CartItem;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.GenerationType.AUTO;

@Serdeable
@Entity
@Table(name = "Discount")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="discount_type",
        discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@SuperBuilder
public abstract class Discount {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long discountId;

    @Column(name = "discount_type", insertable=false, updatable = false)
    private String discountType;

    public abstract Float getDiscountedPrice(CartItem cartItems);

    public abstract boolean isEligible(CartItem cartItems);
}
