package store.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import store.inventory.domain.InventoryItem;
import store.inventory.domain.Product;
import store.inventory.domain.Stock;
import store.inventory.repository.InventoryRepository;

class InventoryRepositoryTest {

    @Test
    void 인벤토리를_정상적으로_저장한다() {
        // given
        InventoryRepository repository = new InventoryRepository();
        Product product = new Product("콜라", 1000);
        Stock stock = new Stock(10, 0);
        InventoryItem inventoryItem = new InventoryItem(product, stock, null);

        // when
        repository.save(inventoryItem);

        // then
        assertThat(repository.findAll()).hasSize(1).containsExactly(inventoryItem);
    }

    @Test
    void 인벤토리에서_상품_이름으로_찾아올_수_있다() {
        // given
        InventoryRepository repository = new InventoryRepository();
        Product product = new Product("사이다", 1000);
        Stock stock = new Stock(10, 0);
        InventoryItem inventoryItem = new InventoryItem(product, stock, null);
        repository.save(inventoryItem);

        // when
        InventoryItem result = repository.findByProductName("사이다").orElse(null);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getProductName()).isEqualTo("사이다");
    }
}