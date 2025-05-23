package com.medimap.microservices.product.controller;

import com.medimap.microservices.product.dto.ProductRequest;
import com.medimap.microservices.product.dto.ProductResponse;
import com.medimap.microservices.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

   @PostMapping("/add")
   @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse addProduct(@RequestBody ProductRequest productRequest) {
       return productService.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getProducts() {
       return productService.getAllProducts();
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse updateProduct(@PathVariable String id,@RequestBody ProductRequest productRequest){
       return productService.updateProduct(id,productRequest);
    }


}
