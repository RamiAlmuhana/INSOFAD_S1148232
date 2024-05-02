package com.example.gamewebshop.dao;

import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.PlacedOrder;
import com.example.gamewebshop.models.Product;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class OrderDAO {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderDAO(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public List<PlacedOrder> getAllOrders(){
        return  this.orderRepository.findAll();
    }


    @Transactional
    public void createOrder(PlacedOrder placedOrder){
        this.userRepository.save(placedOrder.getUser() );

        this.orderRepository.save(placedOrder);

    }


    @Transactional
    public void saveOrderWithProducts(PlacedOrder order, String userEmail) {
        CustomUser user = userRepository.findByEmail(userEmail);
        order.setUser(user);

        double totalPrice = 0.0; // Initialiseren van de totale prijs
        int totalProducts = order.getProducts().size(); // Het aantal producten in de bestelling

        for (Product product : order.getProducts()) {
            totalPrice += product.getPrice().doubleValue(); // Optellen van de prijs van elk product
        }

        order.setTotalPrice(totalPrice); // Instellen van de totale prijs
        order.setTotalProducts(totalProducts); // Instellen van het totale aantal producten

        order.setOrderDate(LocalDateTime.now());

        orderRepository.save(order); // Opslaan van de bestelling in de database, inclusief de bijgewerkte totale prijs
    }







    public List<PlacedOrder> getOrdersByUserId(long userId){
        Optional<List<PlacedOrder>> orderList = this.orderRepository.findByUserId(userId);
        if (orderList.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No products found with that category id"
            );
        }
        return orderList.get();
    }




}
