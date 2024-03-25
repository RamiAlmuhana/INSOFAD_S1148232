package com.example.gamewebshop.dao;

import com.example.gamewebshop.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//maps the product class to the database using the Long type as default of ID's
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<List<Product>> findByCategoryId(long id);
    Optional<Product> findById(long id);

}
