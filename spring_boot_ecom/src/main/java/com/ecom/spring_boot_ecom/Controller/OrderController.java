package com.ecom.spring_boot_ecom.Controller;


import com.ecom.spring_boot_ecom.payload.OrderDTO;
import com.ecom.spring_boot_ecom.payload.OrderRequestDTO;
import com.ecom.spring_boot_ecom.service.OrderService;
import com.ecom.spring_boot_ecom.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private OrderService orderService;

    @PostMapping("/order/users/payment/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderProducts(@PathVariable String paymentMethod, @RequestBody OrderRequestDTO orderRequestDTO){
        String emailId = authUtil.loggedInEmail();
        OrderDTO order = orderService.placeOrder(
                emailId,
                orderRequestDTO.getAddressId(),
                paymentMethod,
                orderRequestDTO.getPgName(),
                orderRequestDTO.getPgPaymentId(),
                orderRequestDTO.getPgStatus(),
                orderRequestDTO.getPgResponseMessage()
        );


        return new ResponseEntity<>(order,HttpStatus.CREATED);

    }
}
