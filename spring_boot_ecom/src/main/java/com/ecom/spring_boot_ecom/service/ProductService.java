package com.ecom.spring_boot_ecom.service;

import com.ecom.spring_boot_ecom.payload.ProductDTO;
import com.ecom.spring_boot_ecom.payload.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface ProductService {
    public ProductDTO addProduct(ProductDTO productDTO,Long categoryId);

    public ProductResponse getAllProducts();

    public ProductResponse searchByCategory(Long categoryId);

    ProductResponse searchProductByKeyword(String keyword);

    ProductDTO updateProduct(ProductDTO productDTO, Long productId);

    ProductDTO deleteProduct(Long productId);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
