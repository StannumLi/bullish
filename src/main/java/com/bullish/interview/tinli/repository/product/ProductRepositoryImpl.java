package com.bullish.interview.tinli.repository.product;

import com.bullish.interview.tinli.model.product.Product;
import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Singleton
public class ProductRepositoryImpl implements ProductRepository {
    private final EntityManager entityManager;

    public ProductRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @ReadOnly
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Product.class, id));
    }

    @Override
    @Transactional
    public Product create(String name, Float price, Long inventory) {
        Product product = Product.builder()
                .name(name)
                .price(price)
                .inventory(inventory)
                .build();
        entityManager.persist(product);
        return product;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        findById(id).ifPresent(entityManager::remove);
    }

    @Override
    @ReadOnly
    public List<Product> findAll() {
        String queryStr = "SELECT p FROM Product as p";
        TypedQuery<Product> query = entityManager.createQuery(queryStr, Product.class);
        return query.getResultList();
    }

}
