package com.ecom.spring_boot_ecom.repo;

import com.ecom.spring_boot_ecom.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CategoryRepo extends JpaRepository<Category,Long> {
        Category findByCategoryName(String categoryName);
}
