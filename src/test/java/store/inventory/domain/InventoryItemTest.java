package store.inventory.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.exception.AdditionalBenefitException;
import store.exception.ErrorMessage;
import store.exception.PromotionNotAvailableException;

public class InventoryItemTest {

    private static final String PRODUCT_NAME = "콜라";
    private InventoryItem inventoryItem;
    private Stock stock;
    private Promotion promotion;

    @BeforeEach
    void setUp() {
        Product product = new Product("콜라", 1000);
        stock = new Stock(10, 5);
        promotion = new Promotion("탄산 2+1", 2, 1, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        inventoryItem = new InventoryItem(product, stock, promotion);
    }

    @ParameterizedTest
    @CsvSource({
            "6, 1",
            "9, 1",
            "4, 1",
            "1, 0"
    })
    void 구매수량에_따른_무료수량을_계산한다(int purchaseQuantity, int expectedFreeQuantity) {
        // when
        int actualFreeQuantity = inventoryItem.calculateFreeQuantity(purchaseQuantity, LocalDate.now());

        // then
        assertThat(actualFreeQuantity).isEqualTo(expectedFreeQuantity);
    }

    @Test
    void 프로모션_재고가_부족하면_예외가_발생한다() {
        // given
        int quantity = 10;

        // when // then
        assertThatThrownBy(() -> inventoryItem.applyPromotions(quantity))
                .isInstanceOf(PromotionNotAvailableException.class)
                .hasMessageContaining(ErrorMessage.PROMOTION_NOT_AVAILABLE.format(PRODUCT_NAME, 7));
    }

    @Test
    void 추가_혜택이_가능하면_예외가_발생한다() {
        //given
        int purchaseQuantity= 2;
        int additionalEligibleQuantity =1;

        // when // then
        assertThatThrownBy(() -> inventoryItem.checkForAdditionalBenefit(purchaseQuantity)).isInstanceOf(
                AdditionalBenefitException.class).hasMessageContaining(
                ErrorMessage.ADDITIONAL_BENEFIT_AVAILABLE.format("콜라", additionalEligibleQuantity));
    }

    @Test
    void 추가_혜택이_가능하지만_재고가_부족하다면_예외를_발생하지_않는다() {
        // given
        int purchaseQuantity = 5;

        // when // then
        assertThatCode(() -> inventoryItem.checkForAdditionalBenefit(purchaseQuantity)).doesNotThrowAnyException();
    }
}
