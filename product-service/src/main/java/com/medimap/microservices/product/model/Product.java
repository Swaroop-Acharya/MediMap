package com.medimap.microservices.product.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Document(value = "product")
public class Product {

    @Id
    private String id;

    @NonNull
    private String name;

    @NonNull
    private  String description;

    @NonNull
    private BigDecimal price;

}
