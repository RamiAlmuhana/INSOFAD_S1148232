package com.example.gamewebshop.controller;

import com.example.gamewebshop.dao.CategoryDAO;
import com.example.gamewebshop.dto.CategoryDTO;
import com.example.gamewebshop.models.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/category")
public class CategoryController {

    private final CategoryDAO categoryDAO;

    public CategoryController(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories(){
        return ResponseEntity.ok(this.categoryDAO.getAllCategories());
    }

    @PostMapping
    public ResponseEntity<String> createCategory(@RequestBody CategoryDTO categoryDTO){
        this.categoryDAO.createCategory(categoryDTO);
        return ResponseEntity.ok("Created a new category named " + categoryDTO.name);
    }
}
