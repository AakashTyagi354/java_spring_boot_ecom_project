package com.ecom.spring_boot_ecom.service;


import com.ecom.spring_boot_ecom.model.Category;
import com.ecom.spring_boot_ecom.payload.CategoryDTO;
import com.ecom.spring_boot_ecom.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse  getAllCategories(Integer pageNumber, Integer pageSize,String sortBy, String sortOrder);

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO deleteCategory(Long categoryId);

    CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO);
}
