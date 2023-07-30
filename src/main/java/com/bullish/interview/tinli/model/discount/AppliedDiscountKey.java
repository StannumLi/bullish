package com.bullish.interview.tinli.model.discount;

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
public class AppliedDiscountKey implements Serializable {
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "discount_id")
    private String discount_id;

}

