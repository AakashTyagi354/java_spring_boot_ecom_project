package com.ecom.spring_boot_ecom.serviceImpl;

import com.ecom.spring_boot_ecom.exceptions.APIException;
import com.ecom.spring_boot_ecom.exceptions.ResourceNotFoundException;
import com.ecom.spring_boot_ecom.model.Category;
import com.ecom.spring_boot_ecom.repo.CategoryRepo;
import com.ecom.spring_boot_ecom.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {



    @Autowired
    private CategoryRepo categoryRepo;

    @Override
    public List<Category> getAllCategories() {
        List<Category> allCategories = categoryRepo.findAll();
        if(allCategories.isEmpty()){
            throw new APIException("No categories found");
        }
        return categoryRepo.findAll();
    }

    @Override
    public void createCategory(Category category) {
        Category savedCategory = categoryRepo.findByCategoryName(category.getCategoryName());
        if (savedCategory != null)
            throw new APIException("Category with name " + category.getCategoryName() + " already exists");


        categoryRepo.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Optional<Category> savedCategoryOptional = categoryRepo.findById(categoryId);
        savedCategoryOptional.orElseThrow(()-> new ResourceNotFoundException("category","categoryId",categoryId));

        categoryRepo.deleteById(categoryId);
        return "Category deleted successfully";
    }

    @Override
    public Category updateCategory(Long categoryId, Category category) {
        Optional<Category> savedCategoryOptional = categoryRepo.findById(categoryId);

       savedCategoryOptional.orElseThrow(()->  new ResourceNotFoundException("category","categoryId",categoryId));


        category.setCategoryId(categoryId);
        return categoryRepo.save(category);




    }

}
