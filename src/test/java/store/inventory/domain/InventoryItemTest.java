package store.inventory.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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
    @CsvSource({"6, 1", "9, 1", "4, 1", "1, 0"})
    void 구매수량에_따른_무료수량을_계산한다(int purchaseQuantity, int expectedFreeQuantity) {
        // when
        int actualFreeQuantity = inventoryItem.calculateFreeQuantity(purchaseQuantity, LocalDate.now());

        // then
        assertThat(actualFreeQuantity).isEqualTo(expectedFreeQuantity);
    }
}
