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
import jakarta.transaction.Transactional;
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
                cart.getCartId(),
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

        Stream<ProductDTO> productStream = cartItems.stream().map(item -> {
            ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return map;
        });

        cartDTO.setProducts(productStream.toList());

        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepo.findAll();
        if (carts.isEmpty()) throw new APIException("No carts found");

        List<CartDTO> cartDTOS = carts.stream()
                .map(cart -> {
                    CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
                    List<CartItem> cartItems = cart.getCartItems();
                    List<ProductDTO> products = cart.getCartItems().stream()
                            .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).toList();
                    cartDTO.setProducts(products);
                    return cartDTO;
                }).toList();

        return cartDTOS;
    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {
        Cart cart = cartRepo.findCartByEmailAndCartId(emailId, cartId);
        if (cart == null) throw new ResourceNotFoundException("Cart", "cartId", cartId);
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        cart.getCartItems().forEach(c -> c.getProduct().setQuantity(c.getQuantity()));
        List<ProductDTO> products = cart.getCartItems().stream()
                .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).toList();
        cartDTO.setProducts(products);
        return cartDTO;
    }


    @Override
    @Transactional // use transtional annotaion when using multiple DB operations
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {
        String emailId = authUtil.loggedInEmail();
        Cart cartFromDB = cartRepo.findCartByEmail(emailId);
        Long cartId = cartFromDB.getCartId();

        Cart cart = cartRepo.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));


        if (product.getQuantity() < quantity) {
            throw new APIException("Please, make an order of the " + product.getProductName() + "less than or equal to the quantity available in stock: " + product.getQuantity());
        }

        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItem == null) {
            throw new APIException("product " + product.getProductName() + " not in cart");
        }
        cartItem.setProductPrice(product.getSpecialPrice());
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItem.setDiscount(product.getDiscount());
        cart.setTotalPrice((cart.getTotalPrice() + (cartItem.getProductPrice() * quantity)));
        if(cartItem.getQuantity()<0){
            throw new APIException("Product quantity in cart cannot be less than zero");
        }
        cartRepo.save(cart);
        CartItem updatedCartItem = cartItemRepo.save(cartItem);

        if (updatedCartItem.getQuantity() == 0) {
            cartItemRepo.deleteById(updatedCartItem.getId());
        }
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<CartItem> cartItems = cart.getCartItems();
        List<ProductDTO> products = cartItems.stream()
                .map(item -> {
                    ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
                    map.setQuantity(item.getQuantity());
                    return map;
                }).toList();
        cartDTO.setProducts(products);

        return cartDTO;

    }

    @Override
    @Transactional
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepo.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId,productId);
        if(cartItem == null){
            throw new ResourceNotFoundException("Product","productId",productId);
        }
        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));
        cartItemRepo.deleteCartItemByProductIdAndCartId(cartId,productId);
        cartRepo.save(cart);
        return "Product removed from cart successfully";
    }

    @Override
    public void updateProductInCarts(Long cartId, Long productId) {
        Cart cart = cartRepo.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItem == null) {
            throw new APIException("Product "+ product.getProductName() +" not available in cart");
        }
        double cartPrice = cart.getTotalPrice() -
                (cartItem.getProductPrice() * cartItem.getQuantity());
        cartItem.setProductPrice(product.getSpecialPrice());
        cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItemRepo.save(cartItem);

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
