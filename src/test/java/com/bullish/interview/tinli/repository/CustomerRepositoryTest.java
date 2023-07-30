package com.bullish.interview.tinli.repository;

import com.bullish.interview.tinli.model.customer.Customer;
import com.bullish.interview.tinli.repository.customer.CustomerRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class CustomerRepositoryTest {

    @Inject
    CustomerRepository customerRepository;

    @Test
    void when_createCustomer_then_saveNewCustomer() {
        Customer expectedCustomer = customerRepository.create("user1");
        Long expectedCustomerId = expectedCustomer.getCustomerId();

        Customer actualCustomer = customerRepository.findById(expectedCustomerId).orElseThrow();
        assertEquals(expectedCustomer, actualCustomer);
    }

    @Test
    void given_existingCustomer_when_delete_then_removeExistingCustomer() {
        Customer existingCustomer = customerRepository.create("user2");
        Long existingCustomerId = existingCustomer.getCustomerId();
        customerRepository.deleteById(existingCustomerId);

        Optional<Customer> deletedCustomer = customerRepository.findById(existingCustomerId);
        assertTrue(deletedCustomer.isEmpty());
    }
}
