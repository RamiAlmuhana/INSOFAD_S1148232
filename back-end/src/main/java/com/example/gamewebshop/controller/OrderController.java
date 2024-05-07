package com.example.gamewebshop.controller;

import com.example.gamewebshop.dao.OrderDAO;
import com.example.gamewebshop.dao.PromoCodeRepository;
import com.example.gamewebshop.dao.UserRepository;
import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.PlacedOrder;
import com.example.gamewebshop.models.Product;
import com.example.gamewebshop.models.PromoCode;
import com.example.gamewebshop.services.PromoCodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/orders")
public class OrderController {

    private final OrderDAO orderDAO;
    private final UserRepository userRepository;
    private final PromoCodeService promoCodeService;
    private final PromoCodeRepository promoCodeRepository;

    public OrderController(OrderDAO orderDAO, UserRepository userRepository, PromoCodeService promoCodeService, PromoCodeRepository promoCodeRepository) {
        this.orderDAO = orderDAO;
        this.userRepository = userRepository;
        this.promoCodeService = promoCodeService;
        this.promoCodeRepository = promoCodeRepository;
    }

    @GetMapping
    public ResponseEntity<List<PlacedOrder>> getAllOrders(){
        return ResponseEntity.ok(this.orderDAO.getAllOrders());
    }


    @GetMapping("/myOrders")
    public ResponseEntity<List<PlacedOrder>> getOrdersByUserPrincipal(Principal principal) {
        if (principal == null) {
            return ResponseEntity.badRequest().build();
        }
        String userEmail = principal.getName();
        CustomUser user = userRepository.findByEmail(userEmail);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<PlacedOrder> orders = this.orderDAO.getOrdersByUserId(user.getId());

        // Voorbeeld: Stel dat je 'totalProducts' al hebt ingesteld in je OrderDAO of ergens anders
        // Anders, hier zou je logica toevoegen om 'totalProducts' te berekenen voor elke bestelling.
        // Bijvoorbeeld, voor elke bestelling, tel het aantal producten en stel 'totalProducts' in.
        // Dit is een eenvoudige demonstratie die ervan uitgaat dat de totalen al berekend zijn.

        return ResponseEntity.ok(orders);
    }




    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody PlacedOrder placedOrder, Principal principal, @RequestParam(required = false) String promoCode) {
        String userEmail = principal.getName();
        double totalPrice = calculateTotalPrice(placedOrder); // Calculate the total price of the order

        if (promoCode != null) {
            Optional<PromoCode> promoCodeOptional = promoCodeService.getPromoCodeByCode(promoCode);
            if (promoCodeOptional.isPresent()) {
                PromoCode code = promoCodeOptional.get();
                if (promoCodeService.isPromoCodeValid(promoCode)) {
                    double discount = calculateDiscount(totalPrice, code); // Calculate the discount based on the promo code
                    totalPrice -= discount; // Apply the discount to the total price of the order

                    code.setMaxUsageCount(code.getMaxUsageCount() - 1); // Decrease the remaining usage count of the promo code
                    promoCodeRepository.save(code); // Save the updated promo code
                } else {
                    return ResponseEntity.badRequest().body("{\"message\": \"Invalid or expired promo code\"}");
                }
            } else {
                return ResponseEntity.badRequest().body("{\"message\": \"Invalid promo code\"}");
            }
        }

        if (totalPrice < 0) {
            totalPrice = 0; // Set the total price to 0 if the discount exceeds the total price
        }

        placedOrder.setTotalPrice(totalPrice); // Set the total price of the order
        this.orderDAO.saveOrderWithProducts(placedOrder, userEmail);
        return ResponseEntity.ok().body("{\"message\": \"Order created successfully\"}");
    }







    private double calculateTotalPrice(PlacedOrder placedOrder) {
        double totalPrice = 0.0;
        for (Product product : placedOrder.getProducts()) {
            totalPrice += product.getPrice().doubleValue();
        }
        return totalPrice;
    }

    private double calculateDiscount(double totalPrice, PromoCode promoCode) {
        double discount = 0.0;
        // Check the type of promo code
        if (promoCode.getType() == PromoCode.PromoCodeType.FIXED_AMOUNT) {
            // If it's a fixed amount, subtract this value directly
            discount = promoCode.getDiscount();
        } else if (promoCode.getType() == PromoCode.PromoCodeType.PERCENTAGE) {
            // If it's a percentage, calculate the discount based on the total price
            discount = totalPrice * (promoCode.getDiscount() / 100);
        }
        return discount;
    }





}
