package com.ecom.spring_boot_ecom.repo;

import com.ecom.spring_boot_ecom.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepo extends JpaRepository<Payment,Long> {

}
