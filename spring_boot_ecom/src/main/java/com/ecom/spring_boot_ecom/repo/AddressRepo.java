package com.ecom.spring_boot_ecom.repo;

import com.ecom.spring_boot_ecom.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AddressRepo extends JpaRepository<Address,Long> {

}
