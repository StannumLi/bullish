package com.bullish.interview.tinli.repository;

import com.bullish.interview.tinli.model.discount.Discount;
import com.bullish.interview.tinli.repository.discount.DiscountRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class DiscountRepositoryTest {

    @Inject
    DiscountRepository discountRepository;

    @Test
    void when_createComboDiscount_then_saveNewComboDiscount() {
        Discount expectedDiscount = discountRepository.createComboDiscount(2, 0.75f);
        Long expectedDiscountId = expectedDiscount.getDiscountId();

        Discount actualDiscount = discountRepository.findById(expectedDiscountId).orElseThrow();
        assertEquals(expectedDiscount, actualDiscount);
    }

    @Test
    void given_existingDiscount_when_delete_then_removeExistingDiscount() {
        Discount existingDiscount = discountRepository.createComboDiscount(3, 0.7f);
        Long existingDiscountId = existingDiscount.getDiscountId();
        discountRepository.deleteDiscountById(existingDiscountId);
        
        Optional<Discount> deletedDiscount = discountRepository.findById(existingDiscountId);
        assertTrue(deletedDiscount.isEmpty());
    }
}
