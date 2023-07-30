package com.bullish.interview.tinli.controller.customer;

import com.bullish.interview.tinli.model.customer.Customer;
import com.bullish.interview.tinli.repository.customer.CustomerRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

@ExecuteOn(TaskExecutors.IO)
@Controller("/customer")
@Tag(name = "Customer API")
public class CustomerController {

    private final CustomerRepository repository;

    public CustomerController(CustomerRepository repository) {
        this.repository = repository;
    }

    @Post("/create")
    @Operation(summary = "Create a customer", description = "Create a new customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created")
    })
    HttpResponse<Customer> create(@Body @Valid CreateCustomerCommand cmd) {
        Customer Customer = repository.create(cmd.getUsername());

        return HttpResponse.created(Customer);
    }

    @Delete("/delete/{customerId}")
    @Operation(summary = "Delete a customer", description = "Delete a customer by customerId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted")
    })
    HttpResponse<?> delete(@PathVariable("customerId") @Parameter(name = "customerId", description = "Customer Id")
                           Long id) {
        repository.deleteById(id);
        return HttpResponse.noContent();
    }

    @Get(value = "/list")
    @Operation(summary = "List all customers", description = "List all customers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all customers")
    })
    List<Customer> list() {
        return repository.findAll();
    }
}
