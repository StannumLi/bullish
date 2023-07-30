package com.bullish.interview.tinli.repository.customer;

import com.bullish.interview.tinli.model.customer.Customer;
import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Singleton
public class CustomerRepositoryImpl implements CustomerRepository {
    private final EntityManager entityManager;

    public CustomerRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @ReadOnly
    public Optional<Customer> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Customer.class, id));
    }

    @Override
    @Transactional
    public Customer create(String username) {
        Customer customer = Customer.builder()
                .username(username)
                .build();
        entityManager.persist(customer);
        return customer;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        findById(id).ifPresent(entityManager::remove);
    }

    @Override
    @ReadOnly
    public List<Customer> findAll() {
        String queryStr = "SELECT u FROM Customer as u";
        TypedQuery<Customer> query = entityManager.createQuery(queryStr, Customer.class);
        return query.getResultList();
    }

}
