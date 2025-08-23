package com.ecom.spring_boot_ecom.service;


import com.ecom.spring_boot_ecom.model.Category;
import com.ecom.spring_boot_ecom.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse getAllCategories();

    void createCategory(Category category);

    String deleteCategory(Long categoryId);

    Category updateCategory(Long categoryId, Category category);
}
