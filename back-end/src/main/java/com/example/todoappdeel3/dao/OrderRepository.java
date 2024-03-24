package com.example.todoappdeel3.dao;

import com.example.todoappdeel3.models.PlacedOrder;
import com.example.todoappdeel3.models.Product;
import org.hibernate.query.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<PlacedOrder, Long> {
    Optional<List<PlacedOrder>> findByUserId(long id);
}
