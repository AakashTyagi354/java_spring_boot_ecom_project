package com.ecom.spring_boot_ecom.service;

import com.ecom.spring_boot_ecom.payload.OrderDTO;
import com.ecom.spring_boot_ecom.payload.OrderRequestDTO;
import jakarta.transaction.Transactional;

public interface OrderService {

    @Transactional
    OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage);
}
