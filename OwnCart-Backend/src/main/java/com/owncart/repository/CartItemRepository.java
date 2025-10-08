package com.owncart.repository;

import com.owncart.model.CartItem;
import com.owncart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCustomer(User customer);
}
