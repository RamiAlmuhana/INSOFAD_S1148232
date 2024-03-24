package com.example.todoappdeel3.dao;

import com.example.todoappdeel3.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//maps the category class to the database using the Long type as default of ID's
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}

