package store.inventory.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PromotionTest {

    private Promotion promotion;

    @BeforeEach
    void setUp() {
        promotion = new Promotion("탄산 2+1", 2, 1, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
    }

    @ParameterizedTest
    @CsvSource({"3, true",      // 최소 조건 충족
            "1, false"      // 최소 조건 불충족
    })
    void 특정_구매수량에서_프로모션적용가능성을_확인한다(int quantity, boolean isApplicable) {
        boolean applicable = promotion.isApplicable(quantity, LocalDate.now());
        assertThat(applicable).isEqualTo(isApplicable);
    }

    @ParameterizedTest
    @CsvSource({"2022-01-01, false", // 기간 외 날짜
            "2023-01-01, true",  // 시작일
            "2023-12-31, true",  // 종료일
            "2024-01-01, false"  // 기간 외 날짜
    })
    void 특정날짜가_프로모션기간에_해당하는지_확인한다(String checkDate, boolean isActive) {
        LocalDate date = LocalDate.parse(checkDate);
        boolean active = promotion.isApplicable(3, LocalDate.now());
        assertThat(active).isEqualTo(isActive);
    }

    @ParameterizedTest
    @CsvSource({"6, 6, 2",     // 기본 2+1 프로모션 적용 가능, 무료 제공 수량 2
            "9, 9, 3",     // 9개 중 2+1 프로모션 세트 3번 적용 가능
            "5, 5, 1",     // 2+1 프로모션 세트 1번 적용 가능
            "7, 3, 1",     // 프로모션 재고 부족, 1 세트만 적용 가능
            "10, 0, 0"     // 프로모션 재고가 없는 경우
    })
    void 구매수량과_프로모션재고를_기반으로_무료수량을_계산한다(int quantity, int availableQuantity, int expectedFreeQuantity) {
        int freeQuantity = promotion.calculateFreeQuantity(quantity, availableQuantity);
        assertThat(freeQuantity).isEqualTo(expectedFreeQuantity);
    }

    @ParameterizedTest
    @CsvSource({"9, 6, 6",      // 9개 필요, 프로모션 세트로 6개 제공 가능
            "6, 4, 3",      // 6개 필요, 재고 한정으로 3개 제공 가능
            "4, 3, 3",      // 4개 필요, 재고 내 최대 3개 제공 가능
            "5, 0, 0"       // 프로모션 재고 없음
    })
    void 구매수량과_프로모션재고를_기반으로_사용가능_프로모션재고를_계산한다(int quantity, int availablePromoStock, int expectedAvailableStock) {
        int availablePromotionStock = promotion.calculateAvailablePromotionStock(quantity, availablePromoStock);
        assertThat(availablePromotionStock).isEqualTo(expectedAvailableStock);
    }

    @ParameterizedTest
    @CsvSource({"5, 1",     // 추가 무료 수량 가능
            "3, 1",     // 기본 세트 충족으로 추가 무료 수량 가능
            "4, 0",     // 무료 제공 불가
            "7, 1"      // 구매량 초과, 추가 무료 수량 가능
    })
    void 구매수량에_따른_추가무료수량_계산한다(int quantity, int expectedRemainingFreeQuantity) {
        int remainingFreeQuantity = promotion.calculateRemainingFreeQuantity(quantity);
        assertThat(remainingFreeQuantity).isEqualTo(expectedRemainingFreeQuantity);
    }
}
