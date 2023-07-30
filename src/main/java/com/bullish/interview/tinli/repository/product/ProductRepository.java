package com.bullish.interview.tinli.repository.product;

import com.bullish.interview.tinli.model.product.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(Long id);

    Product create(@NotBlank String name, @NotNull Float price, @NotNull Long inventory);

    void deleteById(Long id);

    List<Product> findAll();

}
