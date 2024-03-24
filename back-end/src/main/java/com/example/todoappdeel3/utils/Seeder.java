package com.example.todoappdeel3.utils;

import com.example.todoappdeel3.dao.OrderDAO;
import com.example.todoappdeel3.dao.ProductDAO;
import com.example.todoappdeel3.dao.ProductRepository;
import com.example.todoappdeel3.dao.UserRepository;
import com.example.todoappdeel3.models.Category;
import com.example.todoappdeel3.models.CustomUser;
import com.example.todoappdeel3.models.PlacedOrder;
import com.example.todoappdeel3.models.Product;
import org.apache.catalina.User;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class Seeder {
    private ProductDAO productDAO;
    private UserRepository userRepository;
    private OrderDAO orderDAO;
    private ProductRepository productRepository;


    public Seeder(ProductDAO productDAO, UserRepository userRepository, OrderDAO orderDAO, ProductRepository productRepository) {
        this.productDAO = productDAO;
        this.userRepository = userRepository;
        this.orderDAO = orderDAO;
        this.productRepository = productRepository;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event){
        this.seedProducts();
        this.seedUser();
        this.seedOrder();
    }

    private void seedProducts(){
        Category category1 = new Category("Airpods");
        Category category2 = new Category("iPad");
        Product product1 = new Product("AirPods", "The latest and greatest", 249.95, "https://m.media-amazon.com/images/I/71zny7BTRlL._AC_UF894,1000_QL80_.jpg", category1);
        Product product2 = new Product("iPad Pro", "Now with a bigger screen!", 799, "https://m.media-amazon.com/images/I/71zny7BTRlL._AC_UF894,1000_QL80_.jpg", category2);
        this.productDAO.createProduct(product1);
        this.productDAO.createProduct(product2);
    }

    private void seedOrder(){
        Set<Product> products = new HashSet<>(productRepository.findAll());
        CustomUser user = userRepository.findByEmail("test@mail.com");
        PlacedOrder placedOrder1 = new PlacedOrder("Sem", "", "Bersee", "2215LM", 2, "",  user, products);
        PlacedOrder placedOrder2 = new PlacedOrder("Sem", "", "Treur", "2215LM", 2, "", user, products);
        this.orderDAO.createOrder(placedOrder1);
        this.orderDAO.createOrder(placedOrder2);
    }

    private void seedUser(){
        CustomUser customUser = new CustomUser();
        customUser.setName("Rami");
        customUser.setInfix("");
        customUser.setLastName("Al-Muhana");
        customUser.setEmail("test@mail.com");
        customUser.setPassword(new BCryptPasswordEncoder().encode("Test123!"));
        userRepository.save(customUser);
    }
}
