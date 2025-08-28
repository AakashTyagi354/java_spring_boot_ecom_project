package com.ecom.spring_boot_ecom.payload;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long cartItemId;
    private CartDTO cart;

    private ProductDTO productDTO;
    private Integer quantity;
    private double discount;
    private Double discountedPrice;

}
