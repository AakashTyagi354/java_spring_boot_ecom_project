package com.ecom.spring_boot_ecom.Controller;



import com.ecom.spring_boot_ecom.config.AppConstants;
import com.ecom.spring_boot_ecom.payload.CategoryDTO;
import com.ecom.spring_boot_ecom.payload.CategoryResponse;
import com.ecom.spring_boot_ecom.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;





@RestController
@RequestMapping("api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name ="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name ="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORY_BY,required = false) String sortBy,
            @RequestParam(name = "sortOrder",  defaultValue = AppConstants.SORT_ORDER,required = false) String sortOrder
    ) {
        CategoryResponse allCategories = categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(allCategories, HttpStatus.OK);
    }

    @PostMapping("public/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
       CategoryDTO newCategoryDTO =  categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(newCategoryDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable("categoryId") Long categoryId) {
                CategoryDTO status = categoryService.deleteCategory(categoryId);
                return new ResponseEntity<>(status,HttpStatus.OK);
    }

    @PutMapping("public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @PathVariable("categoryId") Long categoryId, @RequestBody CategoryDTO categoryDTO) {
           CategoryDTO status =  categoryService.updateCategory(categoryId, categoryDTO);
            return new ResponseEntity<>(status,HttpStatus.OK);
    }



}
