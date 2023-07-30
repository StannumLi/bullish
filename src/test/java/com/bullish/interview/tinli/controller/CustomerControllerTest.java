package com.bullish.interview.tinli.controller;

import com.bullish.interview.tinli.controller.customer.CreateCustomerCommand;
import com.bullish.interview.tinli.model.customer.Customer;
import com.bullish.interview.tinli.repository.customer.CustomerRepository;
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
public class CustomerControllerTest {

    private BlockingHttpClient blockingClient;

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    CustomerRepository customerRepository;

    @MockBean(CustomerRepository.class)
    CustomerRepository customerRepository() {
        return mock(CustomerRepository.class);
    }

    @BeforeEach
    void setup() {
        blockingClient = client.toBlocking();
    }

    @Test
    void given_missingUsername_when_createCustomer_then_returnBadRequest() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(
                        HttpRequest.POST("/customer/create",
                                CreateCustomerCommand.builder()
                                        .build()
                        )
                )
        );
        assertNotNull(thrown.getResponse());
        assertEquals(BAD_REQUEST, thrown.getStatus());
    }

    @Test
    void when_createCustomer_then_saveNewCustomer() {
        HttpResponse response = blockingClient.exchange(
                HttpRequest.POST("/customer/create",
                        CreateCustomerCommand.builder()
                                .username("user1")
                                .build()
                )
        );
        assertEquals(CREATED, response.getStatus());
        verify(customerRepository, times(1)).create("user1");
    }

    @Test
    void given_zeroCustomer_when_listAllCustomers_then_returnEmptyList() {
        when(customerRepository.findAll()).thenReturn(List.of());
        HttpRequest request = HttpRequest.GET("/customer/list");
        List<Customer> customers = blockingClient.retrieve(request, Argument.of(List.class, Customer.class));
        assertTrue(customers.isEmpty());
    }

    @Test
    void given_existingCustomers_when_listAllCustomers_then_returnCustomerList() {
        when(customerRepository.findAll()).thenReturn(List.of(
                Customer.builder()
                        .customerId(1L)
                        .username("user1")
                        .build()
        ));
        HttpRequest request = HttpRequest.GET("/customer/list");
        List<Customer> customers = blockingClient.retrieve(request, Argument.of(List.class, Customer.class));
        assertEquals(1, customers.size());
        assertEquals(1L, customers.get(0).getCustomerId());
        assertEquals("user1", customers.get(0).getUsername());
    }

    @Test
    void when_deleteCustomer_then_deleteById() {
        HttpResponse response =  blockingClient.exchange(HttpRequest.DELETE("/customer/delete/1"));
        assertEquals(NO_CONTENT, response.getStatus());
        verify(customerRepository, times(1)).deleteById(1L);
    }
}
