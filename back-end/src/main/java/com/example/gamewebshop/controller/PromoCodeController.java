package com.example.gamewebshop.controller;

import com.example.gamewebshop.dao.PromoCodeRepository;
import com.example.gamewebshop.models.PromoCode;
import com.example.gamewebshop.services.PromoCodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/promocodes")
public class PromoCodeController {

    private final PromoCodeService promoCodeService;
    private final PromoCodeRepository promoCodeRepository;

    public PromoCodeController(PromoCodeService promoCodeService, PromoCodeRepository promoCodeRepository) {
        this.promoCodeService = promoCodeService;
        this.promoCodeRepository = promoCodeRepository;
    }

    @GetMapping
    public ResponseEntity<List<PromoCode>> getAllPromoCodes() {
        List<PromoCode> promoCodes = promoCodeService.getAllPromoCodes();
        return ResponseEntity.ok(promoCodes);
    }

    @PostMapping
    public ResponseEntity<PromoCode> addPromoCode(@RequestBody PromoCode promoCode) {
        PromoCode newPromoCode = promoCodeService.addPromoCode(promoCode);
        return ResponseEntity.ok(newPromoCode);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PromoCode> updatePromoCode(@PathVariable Long id, @RequestBody PromoCode promoCodeDetails) {
        PromoCode updatedPromoCode = promoCodeService.updatePromoCode(id, promoCodeDetails);
        return ResponseEntity.ok(updatedPromoCode);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePromoCode(@PathVariable Long id) {
        if (!promoCodeService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        promoCodeService.deletePromoCode(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromoCode> getPromoCodeById(@PathVariable Long id) {
        Optional<PromoCode> promoCode = promoCodeService.getPromoCodeById(id);
        return promoCode.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validatePromoCode(@RequestParam String code) {
        Optional<PromoCode> promoCode = promoCodeService.getPromoCodeByCode(code);
        if (promoCode.isPresent() && promoCodeService.isPromoCodeValid(code)) {
            PromoCode validPromoCode = promoCode.get();
            return ResponseEntity.ok(Map.of(
                    "discount", validPromoCode.getDiscount(),
                    "type", validPromoCode.getType().toString(),
                    "minSpendAmount", validPromoCode.getMinSpendAmount(),
                    "startDate", validPromoCode.getStartDate(),  // Include startDate
                    "expiryDate", validPromoCode.getExpiryDate()
//                    "categoryId", validPromoCode.getCategory() != null ? validPromoCode.getCategory().getId() : null
            ));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/promocode-stats")
    public ResponseEntity<List<Map<String, Object>>> getPromoCodeStats() {
        List<PromoCode> promoCodes = promoCodeRepository.findAll();
        List<Map<String, Object>> promoCodeStats = promoCodes.stream()
                .map(promoCode -> Map.<String, Object>of(
                        "code", promoCode.getCode(),
                        "usageCount", promoCode.getUsageCount(),
                        "totalDiscountAmount", promoCode.getTotalDiscountAmount(),
                        "discount", promoCode.getDiscount(),
                        "expiryDate", promoCode.getExpiryDate(),
                        "startDate", promoCode.getStartDate(),
                        "maxUsageCount", promoCode.getMaxUsageCount(),
                        "type", promoCode.getType(),
                        "minSpendAmount", promoCode.getMinSpendAmount()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(promoCodeStats);
    }

}
