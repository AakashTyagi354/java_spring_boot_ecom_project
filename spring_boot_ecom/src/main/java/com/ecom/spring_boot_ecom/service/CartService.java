package com.ecom.spring_boot_ecom.service;


import com.ecom.spring_boot_ecom.payload.CartDTO;

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);

}
