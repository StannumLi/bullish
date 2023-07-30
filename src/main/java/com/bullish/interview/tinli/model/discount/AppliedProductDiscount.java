package com.bullish.interview.tinli.model.discount;

import com.bullish.interview.tinli.model.product.Product;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Serdeable
@Entity
@Table(name = "AppliedProductDiscount")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppliedProductDiscount {

    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name ="productId")
    private Product product;

    @ManyToOne
    @JoinColumn(name ="discountId")
    private Discount discount;

}
