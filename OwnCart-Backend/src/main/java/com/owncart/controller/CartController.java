package com.owncart.controller;

import com.owncart.model.CartItem;
import com.owncart.model.Product;
import com.owncart.model.User;
import com.owncart.service.CartService;
import com.owncart.service.ProductService;
import com.owncart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    // Add product to cart
    @PostMapping("/add")
    public ResponseEntity<CartItem> addToCart(@RequestParam Long customerId,
                                              @RequestParam Long productId,
                                              @RequestParam int quantity) {
        Optional<User> customer = userService.getUserById(customerId);
        Optional<Product> product = productService.getProductById(productId);
        if (customer.isPresent() && product.isPresent()) {
            return ResponseEntity.ok(cartService.addToCart(customer.get(), product.get(), quantity));
        }
        return ResponseEntity.badRequest().build();
    }

    // Get cart items for customer
    @GetMapping("/{customerId}")
    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable Long customerId) {
        Optional<User> customer = userService.getUserById(customerId);
        return customer.map(user -> ResponseEntity.ok(cartService.getCartItemsByUser(user)))
                       .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update cart item quantity
    @PutMapping("/update/{id}")
    public ResponseEntity<CartItem> updateCartItem(@PathVariable Long id, @RequestParam int quantity) {
        CartItem updatedItem = cartService.updateCartItem(id, quantity);
        if (updatedItem != null) return ResponseEntity.ok(updatedItem);
        return ResponseEntity.notFound().build();
    }

    // Remove cart item
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long id) {
        cartService.removeCartItem(id);
        return ResponseEntity.noContent().build();
    }

    // Clear cart
    @DeleteMapping("/clear/{customerId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long customerId) {
        Optional<User> customer = userService.getUserById(customerId);
        customer.ifPresent(cartService::clearCart);
        return ResponseEntity.noContent().build();
    }
}
