package com.owncart.controller;

import com.owncart.model.CartItem;
import com.owncart.model.Order;
import com.owncart.model.User;
import com.owncart.service.CartService;
import com.owncart.service.OrderService;
import com.owncart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    // Place order
    @PostMapping("/place/{customerId}")
    public ResponseEntity<Order> placeOrder(@PathVariable Long customerId) {
        Optional<User> customer = userService.getUserById(customerId);
        if (customer.isPresent()) {
            List<CartItem> cartItems = cartService.getCartItemsByUser(customer.get());
            if (cartItems.isEmpty()) return ResponseEntity.badRequest().build();
            Order order = orderService.placeOrder(customer.get(), cartItems);
            cartService.clearCart(customer.get());
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }

    // Get all orders for customer
    @GetMapping("/{customerId}")
    public ResponseEntity<List<Order>> getOrders(@PathVariable Long customerId) {
        Optional<User> customer = userService.getUserById(customerId);
        return customer.map(user -> ResponseEntity.ok(orderService.getOrdersByCustomer(user)))
                       .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get order by ID
    @GetMapping("/detail/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update order status
    @PutMapping("/status/{orderId}")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        Order updatedOrder = orderService.updateOrderStatus(orderId, status);
        if (updatedOrder != null) return ResponseEntity.ok(updatedOrder);
        return ResponseEntity.notFound().build();
    }
}
