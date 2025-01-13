package com.medimap.microservices.product.service;

import com.medimap.microservices.product.dto.ProductRequest;
import com.medimap.microservices.product.dto.ProductResponse;
import com.medimap.microservices.product.model.Product;
import com.medimap.microservices.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = Product.builder().
                name(productRequest.name()).
                description(productRequest.description())
                        .price(productRequest.price()).build();
        productRepository.save(product);
        log.info("Product saved successfully");
        return new ProductResponse(product.getId(),product.getName(),product.getDescription(),product.getPrice());
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream().map(product -> new ProductResponse(product.getId(),product.getName(),product.getDescription(),product.getPrice())).collect(Collectors.toList());
    }

    public ProductResponse updateProduct(String id, ProductRequest productRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product with ID " + id + " not found"));

        product.setName(productRequest.name());
        product.setDescription(productRequest.description());
        product.setPrice(productRequest.price());

        productRepository.save(product);
        log.info("Product with ID {} updated successfully", id);

        return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice());
    }

}
