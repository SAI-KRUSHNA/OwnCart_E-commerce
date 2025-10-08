package com.owncart.service;

import com.owncart.model.CartItem;
import com.owncart.model.Product;
import com.owncart.model.User;
import com.owncart.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    // Add product to cart
    public CartItem addToCart(User customer, Product product, int quantity) {
        CartItem cartItem = new CartItem();
        cartItem.setCustomer(customer);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    // Get all items in a user's cart
    public List<CartItem> getCartItemsByUser(User customer) {
        return cartItemRepository.findByCustomer(customer);
    }

    // Update quantity in cart
    public CartItem updateCartItem(Long id, int quantity) {
        return cartItemRepository.findById(id)
                .map(item -> {
                    item.setQuantity(quantity);
                    return cartItemRepository.save(item);
                })
                .orElse(null);
    }

    // Remove item from cart
    public void removeCartItem(Long id) {
        cartItemRepository.deleteById(id);
    }

    // Clear all cart items for user
    public void clearCart(User customer) {
        List<CartItem> items = cartItemRepository.findByCustomer(customer);
        cartItemRepository.deleteAll(items);
    }
}
