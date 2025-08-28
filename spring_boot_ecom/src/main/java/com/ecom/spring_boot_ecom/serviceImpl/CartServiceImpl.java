package com.ecom.spring_boot_ecom.serviceImpl;

import com.ecom.spring_boot_ecom.exceptions.APIException;
import com.ecom.spring_boot_ecom.exceptions.ResourceNotFoundException;
import com.ecom.spring_boot_ecom.model.Cart;
import com.ecom.spring_boot_ecom.model.CartItem;
import com.ecom.spring_boot_ecom.model.Product;
import com.ecom.spring_boot_ecom.payload.CartDTO;
import com.ecom.spring_boot_ecom.payload.ProductDTO;
import com.ecom.spring_boot_ecom.repo.CartItemRepo;
import com.ecom.spring_boot_ecom.repo.CartRepo;
import com.ecom.spring_boot_ecom.repo.ProductRepo;
import com.ecom.spring_boot_ecom.service.CartService;
import com.ecom.spring_boot_ecom.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartRepo cartRepo;

    @Autowired
    AuthUtil authUtil;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CartItemRepo cartItemRepo;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        Cart cart = createCart();
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(
                cart.getCartItems(),
                productId
        );
        if (cartItem != null) {
            throw new APIException("Product already in cart. To change quantity, please update the cart item.");
        }
        if (product.getQuantity() < quantity) {
            throw new APIException("Please, make an order of the " + product.getProductName() + "less than or equal to the quantity available in stock: " + product.getQuantity());
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount((product.getDiscount()));
        newCartItem.setProductPrice(product.getSpecialPrice());

        cartItemRepo.save(newCartItem);

        product.setQuantity(product.getQuantity());

        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
        cartRepo.save(cart);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDTO> productDTOStream = cartItems.stream().map(item -> {
            ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
            productDTO.setQuantity(item.getQuantity());
            return productDTO;
        });

        cartDTO.setProducts(productDTOStream.toList());


        return cartDTO;
    }

    private Cart createCart() {
        Cart userCart = cartRepo.findCartByEmail(authUtil.loggedInEmail());
        if (userCart != null) {
            return userCart;
        }

        Cart cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());

        return cartRepo.save(cart);

    }
}
