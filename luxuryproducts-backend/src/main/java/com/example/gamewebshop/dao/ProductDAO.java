package com.example.gamewebshop.dao;

import com.example.gamewebshop.dto.ProductDTO;
import com.example.gamewebshop.models.Product;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Component
public class ProductDAO {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductDAO(ProductRepository repository, CategoryRepository category) {
        this.productRepository = repository;
        this.categoryRepository = category;
    }

    public List<Product> getAllProducts(){
        return this.productRepository.findAll();
    }

    public Product getProductById(long id){
        Optional<Product> product = this.productRepository.findById(id);

        return product.orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "No product found with that id"
        ));
    }

    public List<Product> getAllProductsByCategory(long id){
        Optional<List<Product>> products =this.productRepository.findByCategoryId(id);

        if (products.get().isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No products found with that category id"
            );
        }

        return products.get();
    }


    @Transactional
    public void createProduct(Product product){
        this.categoryRepository.save(product.getCategory());
        this.productRepository.save(product);
    }

    public void updateProduct(ProductDTO productDTO, Long id){
        Optional<Product> product = this.productRepository.findById(id);

        if (product.isPresent()){
            product.get().setDescription(productDTO.description);
            product.get().setName(productDTO.name);

            this.productRepository.save(product.get());
        }
    }

    public void deleteById(Long id) {
        this.productRepository.deleteById(id);
    }
}
