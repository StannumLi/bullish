package com.bullish.interview.tinli.repository.customer;

import com.bullish.interview.tinli.model.customer.Customer;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    Optional<Customer> findById(Long id);

    Customer create(@NotBlank String username);

    void deleteById(Long id);

    List<Customer> findAll();

}
