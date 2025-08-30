package com.ecom.spring_boot_ecom.serviceImpl;

import com.ecom.spring_boot_ecom.exceptions.APIException;
import com.ecom.spring_boot_ecom.exceptions.ResourceNotFoundException;
import com.ecom.spring_boot_ecom.model.*;
import com.ecom.spring_boot_ecom.payload.OrderDTO;
import com.ecom.spring_boot_ecom.payload.OrderItemDTO;
import com.ecom.spring_boot_ecom.payload.OrderRequestDTO;
import com.ecom.spring_boot_ecom.repo.*;
import com.ecom.spring_boot_ecom.service.CartService;
import com.ecom.spring_boot_ecom.service.OrderService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CartService cartService;

    @Autowired
    ModelMapper modelMapper;


    @Override
    @Transactional
    public OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage) {
        Cart cart = cartRepo.findCartByEmail(emailId);
        if (cart == null) {
            throw new APIException("Cart is empty, cannot place order");
        }

        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));

        Order order = new Order();
        order.setEmail(emailId);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("Order Accepted");
        order.setAddress(address);

        Payment payment = new Payment(paymentMethod, pgPaymentId, pgStatus, pgResponseMessage, pgName);
        payment.setOrder(order);
        payment = paymentRepo.save(payment);
        order.setPayment(payment);

        Order savedOrder = orderRepo.save(order);

        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new APIException("Cart is empty");
        }

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();

            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setOrderProductPrice(cartItem.getProductPrice());
            orderItem.setOrder(savedOrder);

            orderItems.add(orderItem);
        }

        orderItems = orderItemRepository.saveAll(orderItems);

        cart.getCartItems().forEach(item -> {
            int quantity = item.getQuantity();
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() - quantity);
            productRepo.save(product);
            cartService.deleteProductFromCart(cart.getCartId(), item.getProduct().getProductId());
        });

        OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);

        orderItems.forEach(item ->
                orderDTO.getOrderItems()
                        .add(modelMapper.map(item, OrderItemDTO.class)
                        ));

        orderDTO.setAddressId(addressId);



        return orderDTO;
    }
}
