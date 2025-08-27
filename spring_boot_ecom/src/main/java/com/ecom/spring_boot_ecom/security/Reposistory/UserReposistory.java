package com.ecom.spring_boot_ecom.security.Reposistory;

import com.ecom.spring_boot_ecom.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserReposistory extends JpaRepository<User,Long> {
   Optional<User> findByUserName(String username);

   Boolean existsByUserName(String username);

   Boolean existsByEmail(String email);
//   Optional<User> findByUsername(String username);
//   Boolean existsByUserName( String username);
//
//   Boolean existsByEmail(String email);


}
