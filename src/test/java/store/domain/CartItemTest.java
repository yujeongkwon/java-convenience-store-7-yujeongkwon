package store.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static store.exception.ErrorMessage.INSUFFICIENT_STOCK;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.dto.OrderItemDto;
import store.inventory.domain.InventoryItem;
import store.inventory.domain.Product;
import store.inventory.domain.Stock;

class CartItemTest {

    private InventoryItem inventoryItem;

    @BeforeEach
    void setUp() {
        Product cola = new Product("콜라", 1000, null);
        Stock colaStock = new Stock(10, 0);
        inventoryItem = new InventoryItem(cola, colaStock);
    }

    @Test
    void 장바구니_항목을_생성한다() {
        // given
        OrderItemDto orderItemDto = new OrderItemDto("콜라", 2);

        // when
        CartItem cartItem = CartItem.from(orderItemDto, inventoryItem);

        // then
        assertThat(cartItem.getProductName()).isEqualTo("콜라");
    }

    @Test
    void 재고가_부족하면_예외가_발생한다() {
        // given
        OrderItemDto orderItemDto = new OrderItemDto("콜라", 15);

        // when, then
        assertThatThrownBy(() -> CartItem.from(orderItemDto, inventoryItem))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INSUFFICIENT_STOCK.getMessage());
    }
}
