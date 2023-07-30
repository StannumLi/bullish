package com.bullish.interview.tinli.repository;

import com.bullish.interview.tinli.model.discount.Discount;
import com.bullish.interview.tinli.model.product.Product;
import com.bullish.interview.tinli.repository.discount.AppliedProductDiscountRepository;
import com.bullish.interview.tinli.repository.discount.DiscountRepository;
import com.bullish.interview.tinli.repository.product.ProductRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class AppliedProductDiscountRepositoryTest {

    @Inject
    AppliedProductDiscountRepository appliedDiscountRepository;

    @Inject
    ProductRepository productRepository;

    @Inject
    DiscountRepository discountRepository;

    @Test
    void given_productWithoutAppliedDiscount_when_applyDiscount_then_saveAppliedDiscount() {
        Long productId = getProductId("product1");
        Discount discount = getComboDiscount();
        Long discountId = discount.getDiscountId();

        appliedDiscountRepository.applyProductDiscount(productId, discountId);
        Discount appliedDiscount = appliedDiscountRepository.findAppliedDiscountByProduct(productId).orElseThrow();
        assertEquals(discount, appliedDiscount);
    }

    @Test
    void given_productWithAppliedDiscount_when_applyDiscount_then_throwException() {
        Long productId = getProductId("product2");
        Discount discount = getComboDiscount();
        Long discountId = discount.getDiscountId();
        appliedDiscountRepository.applyProductDiscount(productId, discountId);

        Exception exception = assertThrows(Exception.class, () -> {
            appliedDiscountRepository.applyProductDiscount(productId, discountId);
        });
    }

    @Test
    void given_productWithAppliedDiscount_when_revokeDiscount_then_removeAppliedDiscount() {
        Long productId = getProductId("product3");
        Discount discount = getComboDiscount();
        Long discountId = discount.getDiscountId();

        appliedDiscountRepository.applyProductDiscount(productId, discountId);
        appliedDiscountRepository.revokeProductDiscount(productId);
        Optional<Discount> appliedDiscount = appliedDiscountRepository.findAppliedDiscountByProduct(productId);
        assertTrue(appliedDiscount.isEmpty());
    }

    private Discount getComboDiscount() {
        Discount discount = discountRepository.createComboDiscount(2, 0.75f);
        return discount;
    }

    private Long getProductId(String productName) {
        Product product = productRepository.create(productName, 10.5f, 1000L);
        return product.getProductId();
    }
}
