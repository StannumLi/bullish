package com.bullish.interview.tinli.repository;

import com.bullish.interview.tinli.model.cart.CartItem;
import com.bullish.interview.tinli.model.customer.Customer;
import com.bullish.interview.tinli.model.product.Product;
import com.bullish.interview.tinli.repository.cart.CartRepository;
import com.bullish.interview.tinli.repository.customer.CustomerRepository;
import com.bullish.interview.tinli.repository.product.ProductRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class CartRepositoryTest {

    @Inject
    CartRepository cartRepository;

    @Inject
    ProductRepository productRepository;

    @Inject
    CustomerRepository customerRepository;

    @Test
    void given_emptyCart_when_addItem_then_createCartItem() {
        Long customerId = getCustomerId("user1");
        Long productId = getProductId("product1");

        cartRepository.add(customerId, productId, 1L);
        CartItem item = cartRepository.findByKey(customerId, productId).orElseThrow();
        assertEquals(1L, item.getQuantity());
    }

    @Test
    void given_existingCartItem_when_addItem_then_increaseQuantity() {
        Long customerId = getCustomerId("user2");
        Long productId = getProductId("product2");

        cartRepository.add(customerId, productId, 1L);
        cartRepository.add(customerId, productId, 2L);
        CartItem item = cartRepository.findByKey(customerId, productId).orElseThrow();
        assertEquals(3L, item.getQuantity());
    }

    @Test
    void given_emptyCart_when_removeItem_then_throwException() {
        Long customerId = getCustomerId("user3");
        Long productId = getProductId("product3");

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            cartRepository.remove(customerId, productId, 1L);
        });
    }

    @Test
    void given_existingCartItem_when_removeAllItems_then_deleteCartItem() {
        Long customerId = getCustomerId("user4");
        Long productId = getProductId("product5");

        cartRepository.add(customerId, productId, 2L);
        cartRepository.remove(customerId, productId, 2L);

        Optional<CartItem> item = cartRepository.findByKey(customerId, productId);
        assertTrue(item.isEmpty());
    }

    @Test
    void given_existingCartItem_when_removeItem_then_decreaseQuantity() {
        Long customerId = getCustomerId("user6");
        Long productId = getProductId("product6");

        cartRepository.add(customerId, productId, 2L);
        cartRepository.remove(customerId, productId, 1L);
        CartItem item = cartRepository.findByKey(customerId, productId).orElseThrow();
        assertEquals(1L, item.getQuantity());
    }

    private Long getCustomerId(String username) {
        Customer customer = customerRepository.create(username);
        return customer.getCustomerId();
    }

    private Long getProductId(String productName) {
        Product product = productRepository.create(productName, 10.5f, 1000L);
        return product.getProductId();
    }

}
