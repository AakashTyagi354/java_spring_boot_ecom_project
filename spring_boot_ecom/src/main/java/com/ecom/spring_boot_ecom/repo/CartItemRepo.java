package com.ecom.spring_boot_ecom.repo;

import com.ecom.spring_boot_ecom.model.CartItem;
import com.ecom.spring_boot_ecom.payload.CartItemDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem,Long> {

    @Query("select ci from CartItem ci where ci.cart.cartId=?1 AND ci.product.productId=?2")
    CartItem findCartItemByProductIdAndCartId(List<CartItem> cartItems, Long productId);
}
