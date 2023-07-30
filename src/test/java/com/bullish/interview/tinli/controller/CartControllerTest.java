package com.bullish.interview.tinli.controller;

import com.bullish.interview.tinli.controller.cart.UpdateCartCommand;
import com.bullish.interview.tinli.model.cart.CartItem;
import com.bullish.interview.tinli.model.cart.Receipt;
import com.bullish.interview.tinli.model.customer.Customer;
import com.bullish.interview.tinli.model.product.Product;
import com.bullish.interview.tinli.repository.cart.CartRepository;
import com.bullish.interview.tinli.repository.discount.AppliedProductDiscountRepository;
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

import static io.micronaut.http.HttpStatus.BAD_REQUEST;
import static io.micronaut.http.HttpStatus.OK;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MicronautTest
public class CartControllerTest {

    private BlockingHttpClient blockingClient;

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    CartRepository cartRepository;

    @MockBean(CartRepository.class)
    CartRepository cartRepository() {
        return mock(CartRepository.class);
    }

    @Inject
    AppliedProductDiscountRepository appliedDiscountRepository;

    @MockBean(AppliedProductDiscountRepository.class)
    AppliedProductDiscountRepository appliedDiscountRepository() {
        return mock(AppliedProductDiscountRepository.class);
    }

    @BeforeEach
    void setup() {
        blockingClient = client.toBlocking();
    }

    @Test
    void given_missingCustomerId_when_addItem_then_returnBadRequest() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(
                        HttpRequest.POST("/cart/add",
                                UpdateCartCommand.builder()
                                        .productId(2L)
                                        .quantity(10L)
                                        .build()
                        )
                )
        );
        assertNotNull(thrown.getResponse());
        assertEquals(BAD_REQUEST, thrown.getStatus());
    }

    @Test
    void given_missingProductId_when_addItem_then_returnBadRequest() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(
                        HttpRequest.POST("/cart/add",
                                UpdateCartCommand.builder()
                                        .customerId(1L)
                                        .quantity(10L)
                                        .build()
                        )
                )
        );
        assertNotNull(thrown.getResponse());
        assertEquals(BAD_REQUEST, thrown.getStatus());
    }

    @Test
    void given_missingQuantity_when_addItem_then_returnBadRequest() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(
                        HttpRequest.POST("/cart/add",
                                UpdateCartCommand.builder()
                                        .customerId(1L)
                                        .productId(2L)
                                        .build()
                        )
                )
        );
        assertNotNull(thrown.getResponse());
        assertEquals(BAD_REQUEST, thrown.getStatus());
    }

    @Test
    void given_invalidQuantity_when_addItem_then_returnBadRequest() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(
                        HttpRequest.POST("/cart/add",
                                UpdateCartCommand.builder()
                                        .customerId(1L)
                                        .productId(2L)
                                        .quantity(0L)
                                        .build()
                        )
                )
        );
        assertNotNull(thrown.getResponse());
        assertEquals(BAD_REQUEST, thrown.getStatus());
    }

    @Test
    void when_addItem_then_saveItemToCart() {
        HttpResponse response = blockingClient.exchange(
                HttpRequest.POST("/cart/add",
                        UpdateCartCommand.builder()
                                .customerId(1L)
                                .productId(2L)
                                .quantity(10L)
                                .build()
                )
        );
        assertEquals(OK, response.getStatus());
        verify(cartRepository, times(1)).add(1L, 2L, 10L);
    }

    @Test
    void given_emptyCart_when_listItem_then_returnEmptyList() {
        when(cartRepository.findByCustomer(1L)).thenReturn(List.of());
        HttpRequest request = HttpRequest.GET("/cart/list/1");
        List<CartItem> items = blockingClient.retrieve(request, Argument.of(List.class, CartItem.class));
        assertTrue(items.isEmpty());
    }

    @Test
    void given_existingCartItems_when_listItem_then_returnItemList() {
        when(cartRepository.findByCustomer(1L)).thenReturn(List.of(
                CartItem.builder()
                        .customerId(1L)
                        .productId(2L)
                        .quantity(10L)
                        .build()
        ));
        HttpRequest request = HttpRequest.GET("/cart/list/1");
        List<CartItem> items = blockingClient.retrieve(request, Argument.of(List.class, CartItem.class));
        assertEquals(1, items.size());
        assertEquals(1L, items.get(0).getCustomerId());
        assertEquals(2L, items.get(0).getProductId());
        assertEquals(10L, items.get(0).getQuantity());
    }

    @Test
    void given_missingCustomerId_when_removeItem_then_returnBadRequest() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(
                        HttpRequest.POST("/cart/remove",
                                UpdateCartCommand.builder()
                                        .productId(2L)
                                        .quantity(10L)
                                        .build()
                        )
                )
        );
        assertNotNull(thrown.getResponse());
        assertEquals(BAD_REQUEST, thrown.getStatus());
    }

    @Test
    void given_missingProductId_when_removeItem_then_returnBadRequest() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(
                        HttpRequest.POST("/cart/remove",
                                UpdateCartCommand.builder()
                                        .customerId(1L)
                                        .quantity(10L)
                                        .build()
                        )
                )
        );
        assertNotNull(thrown.getResponse());
        assertEquals(BAD_REQUEST, thrown.getStatus());
    }

    @Test
    void given_missingQuantity_when_removeItem_then_returnBadRequest() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(
                        HttpRequest.POST("/cart/remove",
                                UpdateCartCommand.builder()
                                        .customerId(1L)
                                        .productId(2L)
                                        .build()
                        )
                )
        );
        assertNotNull(thrown.getResponse());
        assertEquals(BAD_REQUEST, thrown.getStatus());
    }

    @Test
    void given_invalidQuantity_when_removeItem_then_returnBadRequest() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(
                        HttpRequest.POST("/cart/remove",
                                UpdateCartCommand.builder()
                                        .customerId(1L)
                                        .productId(2L)
                                        .quantity(0L)
                                        .build()
                        )
                )
        );
        assertNotNull(thrown.getResponse());
        assertEquals(BAD_REQUEST, thrown.getStatus());
    }

    @Test
    void when_removeItem_then_removeFromCart() {
        HttpResponse response = blockingClient.exchange(
                HttpRequest.POST("/cart/remove",
                        UpdateCartCommand.builder()
                                .customerId(1L)
                                .productId(2L)
                                .quantity(10L)
                                .build()
                )
        );
        assertEquals(OK, response.getStatus());
        verify(cartRepository, times(1)).remove(1L, 2L, 10L);
    }

    @Test
    void when_calculateReceipt_then_calculateByCustomerId() {
        when(cartRepository.findByCustomer(1L)).thenReturn(List.of(
                CartItem.builder().customerId(1L).productId(2L)
                        .customer(Customer.builder().customerId(1L).username("user1").build())
                        .product(Product.builder().productId(2L).name("product2").price(10f).build())
                        .quantity(1L).build(),
                CartItem.builder().customerId(1L).productId(3L)
                        .customer(Customer.builder().customerId(1L).username("user1").build())
                        .product(Product.builder().productId(3L).name("product3").price(10f).build())
                        .quantity(3L).build()
        ));


        HttpRequest request = HttpRequest.GET("/cart/receipt/1");
        Receipt receipt = blockingClient.retrieve(request, Receipt.class);
        assertNotNull(receipt);
        assertEquals(2, receipt.getPurchases().size());
        assertEquals(40, receipt.getTotalPrice());
    }
}
