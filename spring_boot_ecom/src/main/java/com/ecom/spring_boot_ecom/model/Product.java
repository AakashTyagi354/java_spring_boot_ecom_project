package com.ecom.spring_boot_ecom.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;

    @NotBlank(message = "Product name is required")
    @Size(min = 5, message = "Product name must be at least 5 characters long")
    private String productName;

    @NotBlank(message = "Product description is required")
    @Size(min = 10, message = "Product description must be at least 10 characters long")
    private String productDescription;


    private String manufacturer;

    private Integer quantity;

    private Double price;
    private double discount;
    private double specialPrice;
    private String image;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
