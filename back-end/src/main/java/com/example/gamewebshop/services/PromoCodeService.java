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

    public PromoCodeService(PromoCodeRepository promoCodeRepository) {
        this.promoCodeRepository = promoCodeRepository;
    }

    public List<PromoCode> getAllPromoCodes() {
        return promoCodeRepository.findAll();
    }

    public PromoCode addPromoCode(PromoCode promoCode) {
        return promoCodeRepository.save(promoCode);
    }

    public PromoCode updatePromoCode(Long id, PromoCode promoCodeDetails) {
        Optional<PromoCode> promoCodeOptional = promoCodeRepository.findById(id);
        if (promoCodeOptional.isPresent()) {
            PromoCode existingPromoCode = promoCodeOptional.get();
            existingPromoCode.setCode(promoCodeDetails.getCode());
            existingPromoCode.setDiscount(promoCodeDetails.getDiscount());
            existingPromoCode.setExpiryDate(promoCodeDetails.getExpiryDate());
            existingPromoCode.setMaxUsageCount(promoCodeDetails.getMaxUsageCount());
            existingPromoCode.setMinSpendAmount(promoCodeDetails.getMinSpendAmount()); // Handle new field
            return promoCodeRepository.save(existingPromoCode);
        } else {
            return null; // Or throw an exception
        }
    }

    public void deletePromoCode(Long id) {
        promoCodeRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return promoCodeRepository.existsById(id);
    }

    public Optional<PromoCode> getPromoCodeByCode(String code) {
        return promoCodeRepository.findByCode(code);
    }

    public boolean isPromoCodeValid(String code) {
        Optional<PromoCode> promoCodeOptional = getPromoCodeByCode(code);
        return promoCodeOptional.isPresent() && promoCodeOptional.get().getExpiryDate().isAfter(LocalDateTime.now()) && promoCodeOptional.get().getMaxUsageCount() > 0;
    }

    public Optional<PromoCode> getPromoCodeById(Long id) {
        return promoCodeRepository.findById(id);
    }
}
