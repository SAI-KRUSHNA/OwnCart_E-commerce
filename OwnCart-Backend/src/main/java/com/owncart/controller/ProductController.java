package com.owncart.controller;

import com.owncart.model.Product;
import com.owncart.model.User;
import com.owncart.service.ProductService;
import com.owncart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    // Add product (Seller)
    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@RequestBody Product product, @RequestParam Long sellerId) {
        Optional<User> seller = userService.getUserById(sellerId);
        if (seller.isPresent()) {
            product.setSeller(seller.get());
            return ResponseEntity.ok(productService.addProduct(product));
        }
        return ResponseEntity.badRequest().build();
    }

    // Get all products
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get products by seller
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Product>> getProductsBySeller(@PathVariable Long sellerId) {
        Optional<User> seller = userService.getUserById(sellerId);
        return seller.map(user -> ResponseEntity.ok(productService.getProductsBySeller(user)))
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        Product product = productService.updateProduct(id, updatedProduct);
        if (product != null) return ResponseEntity.ok(product);
        return ResponseEntity.notFound().build();
    }

    // Delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
