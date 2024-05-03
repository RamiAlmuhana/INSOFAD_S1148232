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

        // Totale prijs al berekend buiten de loop
        // Dus we gebruiken gewoon de totale prijs die al is ingesteld
        System.out.println("Total price before setting: " + order.getTotalPrice()); // Controleer de totale prijs

        int totalProducts = order.getProducts().size(); // Het aantal producten in de bestelling
        System.out.println("Total products: " + totalProducts); // Controleer het totale aantal producten

        order.setOrderDate(LocalDateTime.now());

        try {
            orderRepository.save(order); // Opslaan van de bestelling in de database
            System.out.println("Order saved successfully.");
        } catch (Exception e) {
            System.out.println("Error saving order: " + e.getMessage());
        }
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
