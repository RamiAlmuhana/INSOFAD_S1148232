package com.example.gamewebshop.services;

import com.example.gamewebshop.dao.PromoCodeRepository;
import com.example.gamewebshop.models.PromoCode;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PromoCodeService {
    private final PromoCodeRepository promoCodeRepository;

    public List<PromoCode> getAllPromoCodes() {
        return promoCodeRepository.findAll();
    }

    // Voeg een nieuwe promocode toe
    public PromoCode addPromoCode(PromoCode promoCode) {
        return promoCodeRepository.save(promoCode);
    }

    // Werk een bestaande promocode bij
    public PromoCode updatePromoCode(Long id, PromoCode promoCodeDetails) {
        Optional<PromoCode> promoCodeOptional = promoCodeRepository.findById(id);
        if (promoCodeOptional.isPresent()) {
            PromoCode existingPromoCode = promoCodeOptional.get();
            existingPromoCode.setCode(promoCodeDetails.getCode());
            existingPromoCode.setDiscount(promoCodeDetails.getDiscount());
            existingPromoCode.setExpiryDate(promoCodeDetails.getExpiryDate());
            existingPromoCode.setMaxUsageCount(promoCodeDetails.getMaxUsageCount());
            // Voeg andere attributen toe die je wilt bijwerken

            return promoCodeRepository.save(existingPromoCode);
        } else {
            // Return null or throw exception if the promo code with the given id is not found
            return null;
        }
    }

    // Verwijder een bestaande promocode
    public void deletePromoCode(Long id) {
        promoCodeRepository.deleteById(id);
    }

    // Controleer of een promocode bestaat op basis van het ID
    public boolean existsById(Long id) {
        return promoCodeRepository.existsById(id);
    }


    public PromoCodeService(PromoCodeRepository promoCodeRepository) {
        this.promoCodeRepository = promoCodeRepository;
    }

    public Optional<PromoCode> getPromoCodeByCode(String code) {
        return promoCodeRepository.findByCode(code);
    }

    public boolean isPromoCodeValid(String code) {
        Optional<PromoCode> promoCodeOptional = getPromoCodeByCode(code);
        return promoCodeOptional.isPresent() && promoCodeOptional.get().getExpiryDate().isAfter(LocalDateTime.now()) && promoCodeOptional.get().getMaxUsageCount() > 0;
    }

    // Voeg deze methode toe aan je PromoCodeService-klasse
    public Optional<PromoCode> getPromoCodeById(Long id) {
        return promoCodeRepository.findById(id);
    }

}
