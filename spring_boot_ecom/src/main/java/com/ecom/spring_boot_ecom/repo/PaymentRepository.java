package com.ecom.spring_boot_ecom.repo;


import com.ecom.spring_boot_ecom.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
}
