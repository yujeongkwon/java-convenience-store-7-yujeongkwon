package store.inventory.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PromotionTest {

    private Promotion promotion;

    @BeforeEach
    void setUp() {
        promotion = new Promotion("탄산 2+1", 2, 1, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
    }

    @Test
    void 프로모션_날짜_범위_내에서_적용한다() {
        assertThat(promotion.isApplicable(3, LocalDate.now())).isTrue();
    }

    @Test
    void 프로모션_날짜_범위_외에서는_적용하지_않는다() {
        Promotion expiredPromotion = new Promotion("만료된 프로모션", 2, 1, LocalDate.now().minusDays(5),
                LocalDate.now().minusDays(1));
        assertThat(expiredPromotion.isApplicable(3, LocalDate.now())).isFalse();
    }

    @ParameterizedTest
    @CsvSource({
            "10, 3",
            "6, 2",
            "5, 1",
            "2, 0"})
    void 구매_수량에_따라_증정_수량을_계산한다(int purchaseQuantity, int expectedFreeQuantity) {
        int actualFreeQuantity = promotion.calculateFreeQuantity(purchaseQuantity);
        assertThat(actualFreeQuantity).isEqualTo(expectedFreeQuantity);
    }

    @Test
    void 최소_구매_수량_미달시_증정이_없다() {
        int freeQuantity = promotion.calculateFreeQuantity(1);
        assertThat(freeQuantity).isEqualTo(0);
    }
}
