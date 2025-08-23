package com.ecom.spring_boot_ecom.Controller;


import com.ecom.spring_boot_ecom.model.Category;
import com.ecom.spring_boot_ecom.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.List;

@RestController
@RequestMapping("api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("public/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> allCategories = categoryService.getAllCategories();
        return new ResponseEntity<>(allCategories, HttpStatus.OK);
    }

    @PostMapping("public/categories")
    public ResponseEntity<String> createCategory(@Valid @RequestBody Category category) {
        categoryService.createCategory(category);
        return new ResponseEntity<>("Category created successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable("categoryId") Long categoryId) {


                String status = categoryService.deleteCategory(categoryId);
                return new ResponseEntity<>(status,HttpStatus.OK);

    }

    @PutMapping("public/categories/{categoryId}")
    public ResponseEntity<Category> updateCategory(@Valid @PathVariable("categoryId") Long categoryId, @RequestBody Category category) {



           Category status =  categoryService.updateCategory(categoryId, category);
            return new ResponseEntity<>(status,HttpStatus.OK);



    }



}
