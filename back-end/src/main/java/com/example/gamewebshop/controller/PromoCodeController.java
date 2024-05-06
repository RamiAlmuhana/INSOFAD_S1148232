package com.example.gamewebshop.controller;

import com.example.gamewebshop.models.PromoCode;
import com.example.gamewebshop.services.PromoCodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/promocodes")
public class PromoCodeController {

    private final PromoCodeService promoCodeService;

    public PromoCodeController(PromoCodeService promoCodeService) {
        this.promoCodeService = promoCodeService;
    }

    // Endpoint om alle promocodes op te halen
    @GetMapping
    public ResponseEntity<List<PromoCode>> getAllPromoCodes() {
        List<PromoCode> promoCodes = promoCodeService.getAllPromoCodes();
        return ResponseEntity.ok(promoCodes);
    }

    // Endpoint om een nieuwe promocode toe te voegen
    @PostMapping
    public ResponseEntity<PromoCode> addPromoCode(@RequestBody PromoCode promoCode) {
        PromoCode newPromoCode = promoCodeService.addPromoCode(promoCode);
        return ResponseEntity.ok(newPromoCode);
    }

    // Endpoint om een bestaande promocode bij te werken
    @PutMapping("/{id}")
    public ResponseEntity<PromoCode> updatePromoCode(@PathVariable Long id, @RequestBody PromoCode promoCodeDetails) {
        PromoCode updatedPromoCode = promoCodeService.updatePromoCode(id, promoCodeDetails);
        return ResponseEntity.ok(updatedPromoCode);
    }

    // Endpoint om een bestaande promocode te verwijderen
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePromoCode(@PathVariable Long id) {
        // Controleer of de promo-code bestaat
        if (!promoCodeService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        // Verwijder de promo-code
        promoCodeService.deletePromoCode(id);
        return ResponseEntity.ok().build();
    }


    // Endpoint om een promocode op te halen op basis van ID
    @GetMapping("/{id}")
    public ResponseEntity<PromoCode> getPromoCodeById(@PathVariable Long id) {
        Optional<PromoCode> promoCode = promoCodeService.getPromoCodeById(id);
        if (promoCode.isPresent()) {
            return ResponseEntity.ok(promoCode.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
