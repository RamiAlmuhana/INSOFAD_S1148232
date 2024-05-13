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
import java.util.Map;
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
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody PlacedOrder placedOrder, Principal principal, @RequestParam(required = false) String promoCode) {
        String userEmail = principal.getName();
        CustomUser user = userRepository.findByEmail(userEmail);
        if (user == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not found"));
        }
        placedOrder.setUser(user);

        double totalPrice = calculateTotalPrice(placedOrder);
        double discountedPrice = totalPrice;

        // Eerst proberen de promocode uit de request parameter te halen, als die er niet is, uit het PlacedOrder object
        String effectivePromoCode = promoCode != null ? promoCode : placedOrder.getPromoCode();

        if (effectivePromoCode != null && !effectivePromoCode.isEmpty()) {
            Optional<PromoCode> promoCodeOptional = promoCodeService.getPromoCodeByCode(effectivePromoCode);
            if (promoCodeOptional.isPresent() && promoCodeService.isPromoCodeValid(effectivePromoCode)) {
                PromoCode code = promoCodeOptional.get();
                double discount = calculateDiscount(totalPrice, code);
                discountedPrice -= discount;
                if (discountedPrice < 0) {
                    discountedPrice = 0;
                }
                code.setMaxUsageCount(code.getMaxUsageCount() - 1);
                promoCodeRepository.save(code);
                placedOrder.setPromoCode(effectivePromoCode);
            } else {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid or expired promo code"));
            }
        }

        placedOrder.setTotalPrice(totalPrice);
        placedOrder.setDiscountedPrice(discountedPrice);
        orderDAO.saveOrderWithProducts(placedOrder, userEmail);

        return ResponseEntity.ok(Map.of(
                "message", "Order created successfully",
                "totalPrice", totalPrice,
                "discountedPrice", discountedPrice,
                "promoCode", effectivePromoCode != null ? effectivePromoCode : "No promo code used"
        ));
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
