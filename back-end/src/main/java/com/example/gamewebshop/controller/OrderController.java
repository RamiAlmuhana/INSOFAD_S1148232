package com.example.gamewebshop.controller;

import com.example.gamewebshop.dao.OrderDAO;
import com.example.gamewebshop.dao.ProductRepository;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/orders")
public class OrderController {

    private final OrderDAO orderDAO;
    private final UserRepository userRepository;
    private final PromoCodeService promoCodeService;
    private final PromoCodeRepository promoCodeRepository;
    private final ProductRepository productRepository;

    public OrderController(OrderDAO orderDAO, UserRepository userRepository, PromoCodeService promoCodeService, PromoCodeRepository promoCodeRepository, ProductRepository productRepository) {
        this.orderDAO = orderDAO;
        this.userRepository = userRepository;
        this.promoCodeService = promoCodeService;
        this.promoCodeRepository = promoCodeRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public ResponseEntity<List<PlacedOrder>> getAllOrders() {
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

        // Haal volledige productinformatie op, inclusief categorieën
        List<Product> productsWithCategory = placedOrder.getProducts().stream()
                .map(product -> productRepository.findById(product.getId()).orElse(null))
                .filter(product -> product != null)
                .collect(Collectors.toList());

        placedOrder.setProducts(new HashSet<>(productsWithCategory));

        double totalPrice = calculateTotalPrice(placedOrder);
        double discountedPrice = totalPrice;

        // Print producten en categorieën
        placedOrder.getProducts().forEach(product -> {
            System.out.println("Product: " + product.getName() + ", Category: " + (product.getCategory() != null ? product.getCategory().getName() : "No Category"));
        });

        // Check for FPS discount
        boolean hasFPSProduct = placedOrder.getProducts().stream()
                .anyMatch(product -> product.getCategory() != null && "FPS".equalsIgnoreCase(product.getCategory().getName()));
        if (hasFPSProduct) {
            System.out.println("FPS product found, applying FPS_DISCOUNT");
            Optional<PromoCode> fpsPromoCode = promoCodeRepository.findByCode("FPS_DISCOUNT");
            if (fpsPromoCode.isPresent() && promoCodeService.isPromoCodeValid("FPS_DISCOUNT")) {
                double discount = calculateDiscount(totalPrice, fpsPromoCode.get());
                discountedPrice -= discount;
                if (discountedPrice < 0) {
                    discountedPrice = 0;
                }
                fpsPromoCode.get().setMaxUsageCount(fpsPromoCode.get().getMaxUsageCount() - 1);
                promoCodeRepository.save(fpsPromoCode.get());
                placedOrder.setPromoCode("FPS_DISCOUNT");
            }
        }

        // Additional promo code logic
        String effectivePromoCode = promoCode != null ? promoCode : placedOrder.getPromoCode();
        if (effectivePromoCode != null && !effectivePromoCode.isEmpty() && !effectivePromoCode.equals("FPS_DISCOUNT")) {
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
        return placedOrder.getProducts().stream().mapToDouble(Product::getPrice).sum();
    }

    private double calculateDiscount(double totalPrice, PromoCode promoCode) {
        if (promoCode.getType() == PromoCode.PromoCodeType.FIXED_AMOUNT) {
            return promoCode.getDiscount();
        } else if (promoCode.getType() == PromoCode.PromoCodeType.PERCENTAGE) {
            return totalPrice * (promoCode.getDiscount() / 100);
        }
        return 0.0;
    }
}
