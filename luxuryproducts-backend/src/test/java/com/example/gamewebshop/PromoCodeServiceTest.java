package com.example.gamewebshop;

import com.example.gamewebshop.dao.PromoCodeRepository;
import com.example.gamewebshop.models.Category;
import com.example.gamewebshop.models.PromoCode;
import com.example.gamewebshop.services.PromoCodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PromoCodeServiceTest {

    @Mock
    private PromoCodeRepository promoCodeRepository;

    @InjectMocks
    private PromoCodeService promoCodeService;

    private PromoCode promoCode;

    @BeforeEach
    public void setup() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        promoCode = new PromoCode();
        promoCode.setId(1L);
        promoCode.setCode("DISCOUNT10");
        promoCode.setDiscount(10.0);
        promoCode.setExpiryDate(LocalDateTime.now().plusDays(1));
        promoCode.setStartDate(LocalDateTime.now().minusDays(1));
        promoCode.setMaxUsageCount(5);
        promoCode.setType(PromoCode.PromoCodeType.PERCENTAGE);
        promoCode.setMinSpendAmount(50.0);
        promoCode.setUsageCount(0);
        promoCode.setCategory(category);
    }

    @Test
    public void should_return_promoCode_when_getPromoCodeByCode_is_called() {
        when(promoCodeRepository.findByCode(anyString())).thenReturn(Optional.of(promoCode));

        Optional<PromoCode> actualPromoCode = promoCodeService.getPromoCodeByCode("DISCOUNT10");

        assertThat(actualPromoCode.isPresent(), is(true));
        assertThat(actualPromoCode.get().getCode(), is(promoCode.getCode()));
        Mockito.verify(promoCodeRepository, Mockito.times(1)).findByCode(anyString());
    }

    @Test
    public void should_throw_exception_when_promoCode_does_not_exist() {
        when(promoCodeRepository.findByCode(anyString())).thenReturn(Optional.empty());

        Optional<PromoCode> actualPromoCode = promoCodeService.getPromoCodeByCode("INVALID_CODE");

        assertThat(actualPromoCode.isPresent(), is(false));
        Mockito.verify(promoCodeRepository, Mockito.times(1)).findByCode(anyString());
    }

    @Test
    public void should_return_true_when_promoCode_is_valid() {
        when(promoCodeRepository.findByCode(anyString())).thenReturn(Optional.of(promoCode));

        boolean isValid = promoCodeService.isPromoCodeValid("DISCOUNT10");

        assertThat(isValid, is(true));
        Mockito.verify(promoCodeRepository, Mockito.times(1)).findByCode(anyString());
    }

    @Test
    public void should_return_false_when_promoCode_is_expired() {
        promoCode.setExpiryDate(LocalDateTime.now().minusDays(1));
        when(promoCodeRepository.findByCode(anyString())).thenReturn(Optional.of(promoCode));

        boolean isValid = promoCodeService.isPromoCodeValid("DISCOUNT10");

        assertThat(isValid, is(false));
        Mockito.verify(promoCodeRepository, Mockito.times(1)).findByCode(anyString());
    }

    @Test
    public void should_return_false_when_promoCode_maxUsageCount_is_zero() {
        promoCode.setMaxUsageCount(0);
        when(promoCodeRepository.findByCode(anyString())).thenReturn(Optional.of(promoCode));

        boolean isValid = promoCodeService.isPromoCodeValid("DISCOUNT10");

        assertThat(isValid, is(false));
        Mockito.verify(promoCodeRepository, Mockito.times(1)).findByCode(anyString());
    }

    @Test
    public void should_update_promoCode_usageCount_and_totalDiscountAmount() {
        when(promoCodeRepository.findByCode(anyString())).thenReturn(Optional.of(promoCode));
        when(promoCodeRepository.save(Mockito.any(PromoCode.class))).thenReturn(promoCode);

        double totalPrice = 100.0;
        double discount = totalPrice * (promoCode.getDiscount() / 100);
        promoCode.setTotalDiscountAmount(promoCode.getTotalDiscountAmount() + discount);
        promoCode.setUsageCount(promoCode.getUsageCount() + 1);

        PromoCode updatedPromoCode = promoCodeService.updatePromoCode(promoCode.getId(), promoCode);

        assertThat(updatedPromoCode.getUsageCount(), is(1));
        assertThat(updatedPromoCode.getTotalDiscountAmount(), is(discount));
        Mockito.verify(promoCodeRepository, Mockito.times(1)).save(Mockito.any(PromoCode.class));
    }
}
