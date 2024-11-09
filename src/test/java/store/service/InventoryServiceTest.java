package store.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.inventory.domain.Promotion;
import store.inventory.repository.InventoryRepository;
import store.inventory.repository.PromotionRepository;
import store.inventory.service.InventoryService;

class InventoryServiceTest {

    private InventoryRepository inventoryRepository;
    private PromotionRepository promotionRepository;
    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        inventoryRepository = new InventoryRepository();
        promotionRepository = new PromotionRepository();
        inventoryService = new InventoryService(inventoryRepository, promotionRepository);

        promotionRepository.saveAll(
                List.of(new Promotion("탄산2+1", 2, 1, null, null), new Promotion("MD추천상품", 1, 1, null, null),
                        new Promotion("반짝할인", 1, 1, null, null)));
    }

    @Test
    void productsMd_파일로부터_데이터를_불러와_저장한다() throws IOException {
        String filePath = Paths.get("src", "main", "resources", "products.md").toString();

        inventoryService.loadInventory(filePath);

        assertThat(inventoryRepository.findAll()).hasSize(11);
    }
}
