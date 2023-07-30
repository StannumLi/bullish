package com.bullish.interview.tinli.repository;

import com.bullish.interview.tinli.model.product.Product;
import com.bullish.interview.tinli.repository.product.ProductRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class ProductRepositoryTest {

    @Inject
    ProductRepository productRepository;

    @Test
    void when_createProduct_then_saveNewProduct() {
        Product expectedProduct = productRepository.create("product1", 10.5f, 1000L);
        Long expectedProductId = expectedProduct.getProductId();

        Product actualProduct = productRepository.findById(expectedProductId).orElseThrow();
        assertEquals(expectedProduct, actualProduct);
    }

    @Test
    void given_existingProduct_when_delete_then_removeExistingProduct() {
        Product existingProduct = productRepository.create("product2", 10.5f, 1000L);
        Long existingProductId = existingProduct.getProductId();
        productRepository.deleteById(existingProductId);

        Optional<Product> deletedProduct = productRepository.findById(existingProductId);
        assertTrue(deletedProduct.isEmpty());
    }
}
