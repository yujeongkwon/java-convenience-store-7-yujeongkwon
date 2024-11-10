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
import store.order.domain.CartItem;
import store.order.dto.FreeItemDto;
import store.order.dto.OrderItemDto;
import store.order.dto.ReceiptItemDto;

class CartTest {

    private List<InventoryItem> inventoryItems;
    private Cart cart;

    @BeforeEach
    void setUp() {
        Product cola = new Product("콜라", 1000);
        Product soda = new Product("사이다", 1000);
        Stock colaStock = new Stock(10, 0);
        Stock sodaStock = new Stock(8, 0);
        InventoryItem colaItem = new InventoryItem(cola, colaStock, null);
        InventoryItem sodaItem = new InventoryItem(soda, sodaStock, null);
        inventoryItems = List.of(colaItem, sodaItem);

        CartItem colaCartItem = new CartItem("콜라", colaItem, 3, 1);
        CartItem sodaCartItem = new CartItem("사이다", sodaItem, 5, 2);
        cart = new Cart(List.of(colaCartItem, sodaCartItem));
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
        List<OrderItemDto> orderItems = List.of(new OrderItemDto("탄산수", 2));

        // when, then
        assertThatThrownBy(() -> Cart.of(orderItems, inventoryItems, LocalDate.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NOT_FOUND.getMessage());
    }

    @Test
    void 구매한_상품목록을_생성한다() {
        // when
        List<ReceiptItemDto> purchasedItems = cart.createPurchasedItems();

        // then
        assertThat(purchasedItems).hasSize(2);
        assertThat(purchasedItems).extracting("productName").contains("콜라", "사이다");
    }

    @Test
    void 증정품목_리스트를_생성한다() {
        // when
        List<FreeItemDto> freeItems = cart.getFreeItems();

        // then
        assertThat(freeItems).hasSize(2);
        assertThat(freeItems).extracting("quantity").contains(1, 2);
    }

    @Test
    void 총_구매액을_계산한다() {
        // when
        int totalAmount = cart.calculateTotalPrice();

        // then
        assertThat(totalAmount).isEqualTo(8000); // (3*1000 + 5*1000)
    }

    @Test
    void 프로모션_할인액을_계산한다() {
        // when
        int promotionDiscount = cart.calculatePromotionDiscount();

        // then
        assertThat(promotionDiscount).isEqualTo(3000); // (1*1000 + 2*1000)
    }
}
