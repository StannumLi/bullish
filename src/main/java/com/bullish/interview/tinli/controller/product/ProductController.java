package com.bullish.interview.tinli.controller.product;

import com.bullish.interview.tinli.model.product.Product;
import com.bullish.interview.tinli.repository.product.ProductRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

@ExecuteOn(TaskExecutors.IO)
@Controller("/product")
@Tag(name = "Product API")
public class ProductController {

    private final ProductRepository repository;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @Post("/create")
    @Operation(summary = "Create a product", description = "Create a new product with properties")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created")
    })
    HttpResponse<Product> create(@Body @Valid CreateProductCommand cmd) {
        Product product = repository.create(cmd.getName(), cmd.getPrice(), cmd.getInventory());

        return HttpResponse.created(product);
    }

    @Delete("/delete/{id}")
    @Operation(summary = "Delete a product", description = "Delete a product by productId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted a product")
    })
    HttpResponse<?> delete(Long id) {
        repository.deleteById(id);
        return HttpResponse.noContent();
    }

    @Get(value = "/list")
    @Operation(summary = "List all products", description = "List properties of all products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all products")
    })
    List<Product> list() {
        return repository.findAll();
    }
}
