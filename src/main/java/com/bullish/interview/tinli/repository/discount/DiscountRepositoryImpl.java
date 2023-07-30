package com.bullish.interview.tinli.repository.discount;

import com.bullish.interview.tinli.model.discount.ComboDiscount;
import com.bullish.interview.tinli.model.discount.Discount;
import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Singleton
public class DiscountRepositoryImpl implements DiscountRepository {

    private final EntityManager entityManager;

    public DiscountRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @ReadOnly
    public Optional<Discount> findById(Long discountId) {
        return Optional.ofNullable(entityManager.find(Discount.class, discountId));
    }

    @Override
    @ReadOnly
    public List<Discount> findByType(String type) {
        String queryStr = "SELECT d FROM Discount as d WHERE discountType = :type";
        TypedQuery<Discount> query = entityManager.createQuery(queryStr, Discount.class);
        query.setParameter("type", type);
        return query.getResultList();
    }

    @Override
    @ReadOnly
    public List<Discount> findAll() {
        String queryStr = "SELECT d FROM Discount as d";
        TypedQuery<Discount> query = entityManager.createQuery(queryStr, Discount.class);
        return query.getResultList();
    }

    @Override
    @Transactional
    public Discount createComboDiscount(Integer comboSize, Float discount) {
        ComboDiscount comboDiscount = ComboDiscount.builder()
                .discountType(ComboDiscount.DISCRIMINATOR_VALUE)
                .comboSize(comboSize)
                .discount(discount)
                .build();
        entityManager.persist(comboDiscount);
        return comboDiscount;
    }

    @Override
    @Transactional
    public void deleteDiscountById(Long discountId) {
        findById(discountId).ifPresent(entityManager::remove);
    }

}
