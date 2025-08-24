package com.ecom.spring_boot_ecom.serviceImpl;


import com.ecom.spring_boot_ecom.exceptions.ResourceNotFoundException;
import com.ecom.spring_boot_ecom.model.Category;
import com.ecom.spring_boot_ecom.model.Product;
import com.ecom.spring_boot_ecom.payload.ProductDTO;
import com.ecom.spring_boot_ecom.payload.ProductResponse;
import com.ecom.spring_boot_ecom.repo.CategoryRepo;
import com.ecom.spring_boot_ecom.repo.ProductRepo;
import com.ecom.spring_boot_ecom.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private FileServiceImpl fileService;

    @Value("${project.image}")
    private String path;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Product product = modelMapper.map(productDTO, Product.class);
        product.setCategory(category);
        double specialPrice = product.getPrice() - (product.getPrice() * product.getDiscount() / 100);
        product.setSpecialPrice(specialPrice);
        product.setImage("default.png");
        Product savedProduct = productRepo.save(product);

        return modelMapper.map(savedProduct, ProductDTO.class);

    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> products = productRepo.findAll();
        List<ProductDTO> productsDTO = products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productsDTO);


        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        List<Product> products = productRepo.findByCategoryOrderByPriceAsc(category);

        List<ProductDTO> productsDTO = products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productsDTO);


        return productResponse;
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword) {
        // %keyword% -> pattern matching -> any string that has keyword in it will be returned
        List<Product> products = productRepo.findByProductNameLikeIgnoreCase('%' + keyword + '%');

        List<ProductDTO> productsDTO = products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productsDTO);


        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
        Product existingProduct = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        existingProduct.setProductName(productDTO.getProductName());
        existingProduct.setProductDescription(productDTO.getProductDescription());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setDiscount(productDTO.getDiscount());
        double specialPrice = existingProduct.getPrice() - (existingProduct.getPrice() * existingProduct.getDiscount() / 100);
        existingProduct.setSpecialPrice(specialPrice);
        existingProduct.setQuantity(productDTO.getQuantity());

        Product updatedProduct = productRepo.save(existingProduct);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product existingProduct = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        productRepo.delete(existingProduct);

        return modelMapper.map(existingProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        // get product form DB
        Product productFromDB = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        // upload image to server
        // get file name of uploaded image

        String fileName = fileService.uploadImage(path, image);

        // updating the new file name to product
        productFromDB.setImage(fileName);

        // save the updated product to DB
        Product updatedProduct = productRepo.save(productFromDB);

        // return dto after modelmapper product to DTO
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }



}
