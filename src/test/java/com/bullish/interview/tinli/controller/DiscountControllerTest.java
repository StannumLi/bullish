package com.bullish.interview.tinli.controller;

import com.bullish.interview.tinli.controller.discount.ApplyProductDiscountCommand;
import com.bullish.interview.tinli.controller.discount.CreateComboDiscountCommand;
import com.bullish.interview.tinli.model.discount.ComboDiscount;
import com.bullish.interview.tinli.model.discount.Discount;
import com.bullish.interview.tinli.repository.discount.AppliedProductDiscountRepository;
import com.bullish.interview.tinli.repository.discount.DiscountRepository;
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
import java.util.Optional;

import static io.micronaut.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MicronautTest
public class DiscountControllerTest {

    private BlockingHttpClient blockingClient;

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    DiscountRepository discountRepository;

    @MockBean(DiscountRepository.class)
    DiscountRepository discountRepository() {
        return mock(DiscountRepository.class);
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
    void given_missingComboSize_when_createComboDiscount_then_returnBadRequest() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(
                        HttpRequest.POST("/discount/create/comboDiscount",
                                CreateComboDiscountCommand.builder()
                                        .discount(0.75f)
                                        .build()
                        )
                )
        );
        assertNotNull(thrown.getResponse());
        assertEquals(BAD_REQUEST, thrown.getStatus());
    }

    @Test
    void given_missingDiscountRate_when_createComboDiscount_then_returnBadRequest() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(
                        HttpRequest.POST("/discount/create/comboDiscount",
                                CreateComboDiscountCommand.builder()
                                        .comboSize(2)
                                        .build()
                        )
                )
        );
        assertNotNull(thrown.getResponse());
        assertEquals(BAD_REQUEST, thrown.getStatus());
    }

    @Test
    void given_invalidComboSize_when_createComboDiscount_then_returnBadRequest() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(
                        HttpRequest.POST("/discount/create/comboDiscount",
                                CreateComboDiscountCommand.builder()
                                        .comboSize(0)
                                        .discount(0.75f)
                                        .build()
                        )
                )
        );
        assertNotNull(thrown.getResponse());
        assertEquals(BAD_REQUEST, thrown.getStatus());
    }

    @Test
    void given_invalidDiscountRate_when_createComboDiscount_then_returnBadRequest() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(
                        HttpRequest.POST("/discount/create/comboDiscount",
                                CreateComboDiscountCommand.builder()
                                        .comboSize(2)
                                        .discount(0f)
                                        .build()
                        )
                )
        );
        assertNotNull(thrown.getResponse());
        assertEquals(BAD_REQUEST, thrown.getStatus());
    }

    @Test
    void when_createComboDiscount_then_saveNewDiscount() {
        HttpResponse response = blockingClient.exchange(
                HttpRequest.POST("/discount/create/comboDiscount",
                        CreateComboDiscountCommand.builder()
                                .comboSize(2)
                                .discount(0.75f)
                                .build()
                )
        );
        assertEquals(CREATED, response.getStatus());
        verify(discountRepository, times(1)).createComboDiscount(2, 0.75f);
    }

    @Test
    void given_zeroDiscount_when_listComboDiscounts_then_returnEmptyList() {
        when(discountRepository.findByType(anyString())).thenReturn(List.of());
        HttpRequest request = HttpRequest.GET("/discount/list/COMBO");
        List<Discount> discounts = blockingClient.retrieve(request, Argument.of(List.class, Discount.class));
        assertTrue(discounts.isEmpty());
    }

    @Test
    void given_existingDiscounts_when_listComboDiscount_then_returnDiscountList() {
        when(discountRepository.findByType("COMBO")).thenReturn(List.of(
                ComboDiscount.builder()
                        .discountId(1L)
                        .discountType(ComboDiscount.DISCRIMINATOR_VALUE)
                        .comboSize(2)
                        .discount(0.75f)
                        .build()
        ));
        HttpRequest request = HttpRequest.GET("/discount/list/COMBO");
        List<Discount> discounts = blockingClient.retrieve(request, Argument.of(List.class, ComboDiscount.class));
        assertEquals(1, discounts.size());
        ComboDiscount comboDiscount = (ComboDiscount) discounts.get(0);
        assertEquals(2, comboDiscount.getComboSize());
        assertEquals(0.75f, comboDiscount.getDiscount());
    }

    @Test
    void when_deleteComboDiscount_then_deleteById() {
        HttpResponse response =  blockingClient.exchange(HttpRequest.DELETE("/discount/delete/1"));
        assertEquals(NO_CONTENT, response.getStatus());
        verify(discountRepository, times(1)).deleteDiscountById(1L);
    }

    @Test
    void given_missingProductId_when_applyProductDiscount_then_returnBadRequest() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(
                        HttpRequest.POST("/discount/apply/productDiscount",
                                ApplyProductDiscountCommand.builder()
                                        .discountId(3L)
                                        .build()
                        )
                )
        );
        assertNotNull(thrown.getResponse());
        assertEquals(BAD_REQUEST, thrown.getStatus());
    }

    @Test
    void given_missingDiscountId_when_applyProductDiscount_then_returnBadRequest() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(
                        HttpRequest.POST("/discount/apply/productDiscount",
                                ApplyProductDiscountCommand.builder()
                                        .productId(2L)
                                        .build()
                        )
                )
        );
        assertNotNull(thrown.getResponse());
        assertEquals(BAD_REQUEST, thrown.getStatus());
    }

    @Test
    void when_applyProductDiscount_then_saveAppliedDiscountOnProduct() {
        HttpResponse response =  blockingClient.exchange(
                HttpRequest.POST("/discount/apply/productDiscount",
                        ApplyProductDiscountCommand.builder()
                                .productId(2L)
                                .discountId(3L)
                                .build()
                ));
        assertEquals(CREATED, response.getStatus());
        verify(appliedDiscountRepository, times(1)).applyProductDiscount(2L, 3L);
    }

    @Test
    void given_zeroAppliedDiscount_when_listAppliedDiscount_then_returnNotFound() {
        when(appliedDiscountRepository.findAppliedDiscountByProduct(anyLong())).thenReturn(Optional.empty());
        HttpRequest request = HttpRequest.GET("/discount/listApplied/2");
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
                    blockingClient.retrieve(request, Discount.class);
                });
        assertNotNull(thrown.getResponse());
        assertEquals(NOT_FOUND, thrown.getStatus());
    }

    @Test
    void when_revokeAppliedDiscount_then_removeAppliedDiscountFromProduct() {
        HttpResponse response =  blockingClient.exchange(HttpRequest.DELETE("/discount/revoke/productDiscount/2"));
        assertEquals(NO_CONTENT, response.getStatus());
        verify(appliedDiscountRepository, times(1)).revokeProductDiscount(2L);
    }
}
