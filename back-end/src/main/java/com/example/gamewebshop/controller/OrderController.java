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
        double totalPrice = calculateTotalPrice(placedOrder); // Bereken de totale prijs van de bestelling
        System.out.println("Total price before discount: " + totalPrice); // Print de totale prijs voordat de korting wordt toegepast

        if (promoCode != null && promoCodeService.isPromoCodeValid(promoCode)) {
            Optional<PromoCode> promoCodeOptional = promoCodeService.getPromoCodeByCode(promoCode);
            if (promoCodeOptional.isPresent()) {
                PromoCode code = promoCodeOptional.get();
                if (code.getExpiryDate().isAfter(LocalDateTime.now()) && code.getMaxUsageCount() > 0) {
                    // Controleer of de promocode geldig is en nog niet verlopen is
                    double discount = calculateDiscount(totalPrice, code); // Bereken de korting op basis van de promocode
                    totalPrice -= discount; // Pas de korting toe op de totale prijs van de bestelling
                    System.out.println("Discount applied: " + discount); // Print de toegepaste korting
                    System.out.println("Total price after discount: " + totalPrice); // Print de totale prijs na het toepassen van de korting
                    code.setMaxUsageCount(code.getMaxUsageCount() - 1); // Verminder het aantal resterende keren dat de promocode kan worden gebruikt
                }
            }
        }

        placedOrder.setTotalPrice(totalPrice); // Stel de totale prijs van de bestelling in
        System.out.println("Final total price: " + placedOrder.getTotalPrice()); // Print de uiteindelijke totale prijs van de bestelling

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
        if (promoCode.getType() == PromoCode.PromoCodeType.FIXED_AMOUNT) {
            discount = promoCode.getDiscount();
        } else if (promoCode.getType() == PromoCode.PromoCodeType.PERCENTAGE) {
            discount = totalPrice * promoCode.getDiscount();
        }
        return discount;
    }




}
