package store.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static store.exception.messages.ErrorMessage.DUPLICATE_ORDER_ITEM;
import static store.exception.messages.ErrorMessage.NOT_FOUND;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.inventory.domain.InventoryItem;
import store.inventory.domain.Product;
import store.inventory.domain.Stock;
import store.order.domain.Cart;
import store.order.dto.OrderItemDto;

class CartTest {

    private List<InventoryItem> inventoryItems;

    @BeforeEach
    void setUp() {
        Product cola = new Product("콜라", 1000);
        Stock colaStock = new Stock(10, 0);
        inventoryItems = List.of(new InventoryItem(cola, colaStock, null));
    }

    @Test
    void 장바구니를_생성한다() {
        // given
        List<OrderItemDto> orderItems = List.of(new OrderItemDto("콜라", 2));

        // when
        Cart cart = Cart.of(orderItems, inventoryItems, LocalDate.now());

        // then
        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().get(0).getProductName()).isEqualTo("콜라");
    }

    @Test
    void 중복된_상품이_있으면_예외가_발생한다() {
        // given
        List<OrderItemDto> orderItems = List.of(
                new OrderItemDto("콜라", 2),
                new OrderItemDto("콜라", 3)
        );

        // when, then
        assertThatThrownBy(() -> Cart.of(orderItems, inventoryItems, LocalDate.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(DUPLICATE_ORDER_ITEM.getMessage());
    }

    @Test
    void 존재하지_않는_상품이_있으면_예외가_발생한다() {
        // given
        List<OrderItemDto> orderItems = List.of(new OrderItemDto("사이다", 2));

        // when, then
        assertThatThrownBy(() -> Cart.of(orderItems, inventoryItems, LocalDate.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NOT_FOUND.getMessage());
    }
}
