package store.order.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static store.exception.messages.ErrorMessage.DUPLICATE_ORDER_ITEM;
import static store.exception.messages.ErrorMessage.NOT_FOUND;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.inventory.domain.InventoryItem;
import store.inventory.domain.Product;
import store.inventory.domain.Stock;
import store.order.dto.OrderItemDto;

class CartValidatorTest {

    private List<InventoryItem> inventoryItems;

    @BeforeEach
    void setUp() {
        Product cola = new Product("콜라", 1000);
        Product soda = new Product("사이다", 1000);
        Stock colaStock = new Stock(10, 5);
        Stock sodaStock = new Stock(8, 5);
        InventoryItem colaItem = new InventoryItem(cola, colaStock, null);
        InventoryItem sodaItem = new InventoryItem(soda, sodaStock, null);
        inventoryItems = List.of(colaItem, sodaItem);
    }

    @Test
    void 중복된_상품이_있으면_예외가_발생한다() {
        // given
        List<OrderItemDto> orderItems = List.of(new OrderItemDto("콜라", 2), new OrderItemDto("콜라", 3));

        // when, then
        assertThatThrownBy(() -> CartValidator.validateDuplicate(orderItems)).isInstanceOf(
                IllegalArgumentException.class).hasMessage(DUPLICATE_ORDER_ITEM.getMessage());
    }

    @Test
    void 존재하지_않는_상품이_있으면_예외가_발생한다() {
        // when, then
        assertThatThrownBy(() -> CartValidator.findInventoryItem("탄산수", inventoryItems)).isInstanceOf(
                IllegalArgumentException.class).hasMessage(NOT_FOUND.getMessage());
    }

    @Test
    void 존재하는_상품이_있으면_예외가_발생하지_않는다() {
        // when
        InventoryItem foundItem = CartValidator.findInventoryItem("콜라", inventoryItems);

        // then
        assertThat(foundItem).isNotNull();
        assertThat(foundItem.getProductName()).isEqualTo("콜라");
    }
}
