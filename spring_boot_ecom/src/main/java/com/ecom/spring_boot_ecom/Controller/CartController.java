package com.ecom.spring_boot_ecom.Controller;

import com.ecom.spring_boot_ecom.model.Cart;
import com.ecom.spring_boot_ecom.payload.CartDTO;
import com.ecom.spring_boot_ecom.repo.CartRepo;
import com.ecom.spring_boot_ecom.service.CartService;
import com.ecom.spring_boot_ecom.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    CartRepo cartRepo;

    @Autowired
    AuthUtil authUtil;

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId, @PathVariable Integer quantity) {
        CartDTO cartDTO = cartService.addProductToCart(productId, quantity);

        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getCarts() {
        List<CartDTO> cartDTOS = cartService.getAllCarts();
        return new ResponseEntity<List<CartDTO>>(cartDTOS, HttpStatus.FOUND);
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getCartById() {
        String emailId = authUtil.loggedInEmail();
        Cart cart = cartRepo.findCartByEmail(emailId);
        Long cartId = cart.getCartId();

        CartDTO cartDTO = cartService.getCart(emailId,cartId);

        return new ResponseEntity<CartDTO>(cartDTO,HttpStatus.OK);
    }

}
