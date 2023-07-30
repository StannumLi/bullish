package com.bullish.interview.tinli.repository.cart;

import com.bullish.interview.tinli.model.cart.CartItem;
import com.bullish.interview.tinli.model.cart.CartItemKey;
import com.bullish.interview.tinli.model.customer.Customer;
import com.bullish.interview.tinli.model.product.Product;
import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Singleton
public class CartRepositoryImpl implements CartRepository {

    private final EntityManager entityManager;

    public CartRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @ReadOnly
    public List<CartItem> findByCustomer(Long customerId) {
        String queryStr = "SELECT i FROM CartItem as i WHERE customerId = :customerId";
        TypedQuery<CartItem> query = entityManager.createQuery(queryStr, CartItem.class);
        query.setParameter("customerId", customerId);
        return query.getResultList();
    }

    @Override
    @ReadOnly
    public Optional<CartItem> findByKey(Long customerId, Long productId) {
        CartItemKey key = CartItemKey.builder()
                .customerId(customerId)
                .productId(productId)
                .build();
        return Optional.ofNullable(entityManager.find(CartItem.class, key));
    }

    @Override
    @Transactional
    public void deleteByKey(Long customerId, Long productId) {
        findByKey(customerId, productId).ifPresent(entityManager::remove);
    }

    @Override
    @Transactional
    public void add(Long customerId, Long productId, Long quantity) {
        Optional<CartItem> existingItem = findByKey(customerId, productId);
        if (existingItem.isPresent()) {
            Long newQuantity = existingItem.get().getQuantity() + quantity;
            String updateStr = "UPDATE CartItem i SET quantity = :newQuantity WHERE customerId = :customerId and productId = :productId";
            Query query = entityManager.createQuery(updateStr)
                    .setParameter("newQuantity", newQuantity)
                    .setParameter("customerId", customerId)
                    .setParameter("productId", productId);
            query.executeUpdate();
            entityManager.flush();
            entityManager.clear();
        } else {
            CartItem item = CartItem.builder()
                    .customer(entityManager.getReference(Customer.class, customerId))
                    .product(entityManager.getReference(Product.class, productId))
                    .customerId(customerId)
                    .productId(productId)
                    .quantity(quantity)
                    .build();
            entityManager.persist(item);
        }

    }

    @Override
    @Transactional
    public void remove(Long customerId, Long productId, Long quantity) {
        CartItem existingItem = findByKey(customerId, productId)
                .orElseThrow();

        Long newQuantity = existingItem.getQuantity() - quantity;
        if (newQuantity < 0) {
            throw new RuntimeException("Insufficient cart items");
        }

        if (newQuantity == 0) {
            deleteByKey(customerId, productId);
            return;
        }

        String updateStr = "UPDATE CartItem i SET quantity = :quantity WHERE customerId = :customerId and productId = :productId";
        entityManager.createQuery(updateStr)
                .setParameter("quantity", newQuantity)
                .setParameter("customerId", customerId)
                .setParameter("productId", productId)
                .executeUpdate();
        entityManager.flush();
        entityManager.clear();
    }
}
