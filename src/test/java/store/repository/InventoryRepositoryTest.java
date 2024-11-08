package store.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import store.domain.Inventory;
import store.domain.Product;
import store.domain.Stock;

class InventoryRepositoryTest {

    @Test
    void 인벤토리를_정상적으로_저장한다() {
        // given
        InventoryRepository repository = new InventoryRepository();
        Product product = new Product("콜라", 1000, null);
        Stock stock = new Stock(10, 0);
        Inventory inventory = new Inventory(product, stock);

        // when
        repository.save(inventory);

        // then
        assertThat(repository.findAll()).hasSize(1).containsExactly(inventory);
    }

    @Test
    void 인벤토리에서_상품_이름으로_찾아올_수_있다() {
        // given
        InventoryRepository repository = new InventoryRepository();
        Product product = new Product("사이다", 1000, null);
        Stock stock = new Stock(10, 0);
        Inventory inventory = new Inventory(product, stock);
        repository.save(inventory);

        // when
        Inventory result = repository.findByProductName("사이다").orElse(null);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getProductName()).isEqualTo("사이다");
    }
}