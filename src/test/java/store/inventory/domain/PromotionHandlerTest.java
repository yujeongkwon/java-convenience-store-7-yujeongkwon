package store.inventory.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static store.exception.messages.UserPromotionMessage.ADDITIONAL_BENEFIT_AVAILABLE;
import static store.exception.messages.UserPromotionMessage.PROMOTION_NOT_AVAILABLE;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.exception.AdditionalBenefitException;
import store.exception.PromotionNotAvailableException;

public class PromotionHandlerTest {

    private static final String PRODUCT_NAME = "콜라";
    private PromotionHandler promotionHandler;
    private Stock stock;
    private Promotion promotion;

    @BeforeEach
    void setUp() {
        stock = new Stock(10, 5); // 10 general stock, 5 promotion stock
        promotion = new Promotion("탄산 2+1", 2, 1, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        promotionHandler = new PromotionHandler(promotion, stock);
    }

    @ParameterizedTest
    @CsvSource({"6, 1", "9, 1", "4, 1", "1, 0"})
    void 구매수량에_따른_무료수량을_계산한다(int purchaseQuantity, int expectedFreeQuantity) {
        // when
        int actualFreeQuantity = promotionHandler.calculateFreeQuantity(purchaseQuantity);

        // then
        assertThat(actualFreeQuantity).isEqualTo(expectedFreeQuantity);
    }

    @Test
    void 프로모션_재고가_부족하면_예외가_발생한다() {
        // given
        int quantity = 10;

        // when // then
        assertThatThrownBy(() -> promotionHandler.applyDiscount(quantity, PRODUCT_NAME)).isInstanceOf(
                        PromotionNotAvailableException.class)
                .hasMessageContaining(PROMOTION_NOT_AVAILABLE.format(PRODUCT_NAME, 7));
    }

    @Test
    void 추가_혜택이_가능하면_예외가_발생한다() {
        // given
        int purchaseQuantity = 2;
        int additionalEligibleQuantity = 1;

        // when // then
        assertThatThrownBy(
                () -> promotionHandler.checkAdditionalBenefitEligibility(purchaseQuantity, PRODUCT_NAME)).isInstanceOf(
                        AdditionalBenefitException.class)
                .hasMessageContaining(ADDITIONAL_BENEFIT_AVAILABLE.format(PRODUCT_NAME, additionalEligibleQuantity));
    }

    @Test
    void 추가_혜택이_가능하지만_재고가_부족하다면_예외를_발생하지_않는다() {
        // given
        int purchaseQuantity = 5;

        // when // then
        assertThatCode(() -> promotionHandler.checkAdditionalBenefitEligibility(purchaseQuantity,
                PRODUCT_NAME)).doesNotThrowAnyException();
    }

    @Test
    void 프로모션_적용_여부를_확인한다() {
        // given
        int quantity = 2;
        LocalDate today = LocalDate.now();

        // when
        boolean isApplicable = promotionHandler.isApplicable(quantity, today);

        // then
        assertThat(isApplicable).isTrue();
    }

    @Test
    void 프로모션_기간이_아니면_프로모션이_적용되지_않는다() {
        // given
        int quantity = 2;
        LocalDate today = LocalDate.now().plusDays(5);

        // when
        boolean isApplicable = promotionHandler.isApplicable(quantity, today);

        // then
        assertThat(isApplicable).isFalse();
    }
}
