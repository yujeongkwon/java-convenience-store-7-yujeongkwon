package store.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import store.inventory.domain.Promotion;
import store.inventory.repository.PromotionRepository;

class PromotionRepositoryTest {

    @Test
    void 프로모션을_정상적으로_저장한다() {
        // given
        PromotionRepository repository = new PromotionRepository();
        Promotion promo1 = new Promotion("탄산2+1", 2, 1,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31));
        Promotion promo2 = new Promotion("MD추천상품", 1, 1,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31));

        // when
        repository.saveAll(List.of(promo1, promo2));

        // then
        assertThat(repository.findAll())
                .hasSize(2)
                .containsExactly(promo1, promo2);
    }
}