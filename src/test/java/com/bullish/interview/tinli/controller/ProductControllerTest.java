package com.bullish.interview.tinli.controller;

import com.bullish.interview.tinli.controller.product.CreateProductCommand;
import com.bullish.interview.tinli.model.product.Product;
import com.bullish.interview.tinli.repository.product.ProductRepository;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.micronaut.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MicronautTest
public class ProductControllerTest {

    private BlockingHttpClient blockingClient;

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    ProductRepository productRepository;

    @MockBean(ProductRepository.class)
    ProductRepository productRepository() {
        return mock(ProductRepository.class);
    }

    @BeforeEach
    void setup() {
        blockingClient = client.toBlocking();
    }

    @Test
    void given_missingProductName_when_createProduct_then_returnBadRequest() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(
                        HttpRequest.POST("/product/create",
                                CreateProductCommand.builder()
                                        .price(10.0f)
                                        .inventory(1000L)
                                        .build()
                        )
                )
        );
        assertNotNull(thrown.getResponse());
        assertEquals(BAD_REQUEST, thrown.getStatus());
    }

    @Test
    void given_missingUnitPrice_when_createProduct_then_returnBadRequest() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(
                        HttpRequest.POST("/product/create",
                                CreateProductCommand.builder()
                                        .name("product1")
                                        .inventory(1000L)
                                        .build()
                        )
                )
        );
        assertNotNull(thrown.getResponse());
        assertEquals(BAD_REQUEST, thrown.getStatus());
    }

    @Test
    void given_invalidUnitPrice_when_createProduct_then_returnBadRequest() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(
                        HttpRequest.POST("/product/create",
                                CreateProductCommand.builder()
                                        .name("product1")
                                        .price(0f)
                                        .inventory(1000L)
                                        .build()
                        )
                )
        );
        assertNotNull(thrown.getResponse());
        assertEquals(BAD_REQUEST, thrown.getStatus());
    }

    @Test
    void given_invalidInventory_when_createProduct_then_returnBadRequest() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(
                        HttpRequest.POST("/product/create",
                                CreateProductCommand.builder()
                                        .name("product1")
                                        .price(10.0f)
                                        .inventory(-1L)
                                        .build()
                        )
                )
        );
        assertNotNull(thrown.getResponse());
        assertEquals(BAD_REQUEST, thrown.getStatus());
    }

    @Test
    void when_createProduct_then_saveNewProduct() {
        HttpResponse response = blockingClient.exchange(
                HttpRequest.POST("/product/create",
                        CreateProductCommand.builder()
                                .name("product1")
                                .price(10.0f)
                                .inventory(100L)
                                .build()
                )
        );
        assertEquals(CREATED, response.getStatus());
        verify(productRepository, times(1)).create("product1", 10.0f, 100L);
    }

    @Test
    void given_zeroProducts_when_listAllProducts_then_returnEmptyList() {
        when(productRepository.findAll()).thenReturn(List.of());
        HttpRequest request = HttpRequest.GET("/product/list");
        List<Product> products = blockingClient.retrieve(request, Argument.of(List.class, Product.class));
        assertTrue(products.isEmpty());
    }

    @Test
    void given_existingProducts_when_listAllProducts_then_returnProductList() {
        when(productRepository.findAll()).thenReturn(List.of(Product.builder()
                        .productId(100L)
                        .price(10.5f)
                .build()));
        HttpRequest request = HttpRequest.GET("/product/list");
        List<Product> products = blockingClient.retrieve(request, Argument.of(List.class, Product.class));
        assertEquals(1, products.size());
        assertEquals(100L, products.get(0).getProductId());
        assertEquals(10.5f, products.get(0).getPrice());
    }

    @Test
    void when_deleteProduct_then_deleteById() {
        HttpResponse response =  blockingClient.exchange(HttpRequest.DELETE("/product/delete/1"));
        assertEquals(NO_CONTENT, response.getStatus());
        verify(productRepository, times(1)).deleteById(1L);
    }

}
