package com.ecom.spring_boot_ecom.serviceImpl;

import com.ecom.spring_boot_ecom.exceptions.APIException;
import com.ecom.spring_boot_ecom.exceptions.ResourceNotFoundException;
import com.ecom.spring_boot_ecom.model.Category;
import com.ecom.spring_boot_ecom.payload.CategoryDTO;
import com.ecom.spring_boot_ecom.payload.CategoryResponse;
import com.ecom.spring_boot_ecom.repo.CategoryRepo;
import com.ecom.spring_boot_ecom.service.CategoryService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {



    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories() {
        List<Category> allCategories = categoryRepo.findAll();
        if(allCategories.isEmpty()){
            throw new APIException("No categories found");
        }
        List<CategoryDTO> categoryDTOS = allCategories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class)).toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);

        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);

        Category savedCategory = categoryRepo.findByCategoryName(category.getCategoryName());
        if (savedCategory != null)
            throw new APIException("Category with name " + category.getCategoryName() + " already exists");


        Category newCategory =  categoryRepo.save(category);
        return modelMapper.map(newCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Optional<Category> savedCategoryOptional = categoryRepo.findById(categoryId);
        savedCategoryOptional.orElseThrow(()-> new ResourceNotFoundException("category","categoryId",categoryId));

        categoryRepo.deleteById(categoryId);
       Category deletedCategory =  savedCategoryOptional.get();
        return modelMapper.map(deletedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        Optional<Category> savedCategoryOptional = categoryRepo.findById(categoryId);

       savedCategoryOptional.orElseThrow(()->  new ResourceNotFoundException("category","categoryId",categoryId));


        categoryDTO.setCategoryId(categoryId);
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category updatedCategory = categoryRepo.save(category);
        return modelMapper.map(updatedCategory, CategoryDTO.class);
    }

}
