package com.bullish.interview.tinli.repository.discount;

import com.bullish.interview.tinli.model.discount.AppliedProductDiscount;
import com.bullish.interview.tinli.model.discount.Discount;
import com.bullish.interview.tinli.model.product.Product;
import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.Optional;

@Singleton
public class AppliedProductDiscountRepositoryImpl implements AppliedProductDiscountRepository {

    private final EntityManager entityManager;

    public AppliedProductDiscountRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @ReadOnly
    public Optional<AppliedProductDiscount> findById(Long productId) {
        return Optional.ofNullable(entityManager.find(AppliedProductDiscount.class, productId));
    }

    @Override
    @ReadOnly
    public Optional<Discount> findAppliedDiscountByProduct(Long productId) {
        Optional<AppliedProductDiscount> appliedProductDiscount = findById(productId);
        if (appliedProductDiscount.isPresent()) {
            return Optional.ofNullable(appliedProductDiscount.get().getDiscount());
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public AppliedProductDiscount applyProductDiscount(Long productId, Long discountId) {
        AppliedProductDiscount appliedProductDiscount = AppliedProductDiscount.builder()
                .product(entityManager.getReference(Product.class, productId))
                .discount(entityManager.getReference(Discount.class, discountId))
                .build();
        entityManager.persist(appliedProductDiscount);
        return appliedProductDiscount;
    }

    @Override
    @Transactional
    public void revokeProductDiscount(Long productId) {
        findById(productId).ifPresent(entityManager::remove);
    }
}
