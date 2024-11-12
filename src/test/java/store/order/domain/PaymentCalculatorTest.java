package store.order.domain;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.order.dto.ReceiptDto;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentCalculatorTest {

    @ParameterizedTest(name = "총액: {0}, 프로모션 할인: {1}, 멤버십 상태: {2} -> 멤버십 할인: {3}, 최종 금액: {4}")
    @CsvSource({
            "10000, 2000, true, 2400, 5600",
            "5000, 1000, true, 1200, 2800",
            "12000, 3000, true, 2700, 6300",
            "10000, 2000, false, 0, 8000",
            "15000, 5000, true, 3000, 7000",
            "5000, 1000, false, 0, 4000"
    })
    void 멤버십_상태와_프로모션_할인을_반영하여_최종_금액을_계산한다(int totalAmount, int promotionDiscount, boolean membershipStatus, int expectedMembershipDiscount, int expectedFinalAmount) {
        // given
        Cart cart = createMockCart(totalAmount, promotionDiscount);
        PaymentCalculator calculator = new PaymentCalculator(cart, membershipStatus);

        // when
        ReceiptDto receipt = calculator.generateReceipt();

        // then
        assertThat(receipt.membershipDiscount()).isEqualTo(expectedMembershipDiscount);
        assertThat(receipt.finalAmount()).isEqualTo(expectedFinalAmount);
    }

    @Test
    void 멤버십_할인_상한을_초과하지_않는_경우_상한_내의_금액을_반환한다() {
        // given
        Cart cart = createMockCart(10000, 2000);
        PaymentCalculator calculator = new PaymentCalculator(cart, true);

        // when
        int membershipDiscount = calculator.generateReceipt().membershipDiscount();

        // then
        assertThat(membershipDiscount).isEqualTo(2400);
    }

    @Test
    void 멤버십_할인이_상한을_초과할_경우_상한_금액을_반환한다() {
        // given
        Cart cart = createMockCart(40000, 5000);
        PaymentCalculator calculator = new PaymentCalculator(cart, true);

        // when
        int membershipDiscount = calculator.generateReceipt().membershipDiscount();

        // then
        assertThat(membershipDiscount).isEqualTo(8000);
    }

    private Cart createMockCart(int totalAmount, int promotionDiscount) {
        return new Cart(List.of()) {
            @Override
            public int calculateTotalPrice() {
                return totalAmount;
            }

            @Override
            public int calculatePromotionDiscount() {
                return promotionDiscount;
            }
        };
    }
}
