package com.ecom.spring_boot_ecom.security.Reposistory;

import com.ecom.spring_boot_ecom.model.AppRole;
import com.ecom.spring_boot_ecom.model.Role;
import com.ecom.spring_boot_ecom.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleReposistory extends JpaRepository<Role,Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
