package store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.domain.Inventory;
import store.domain.Promotion;
import store.repository.InventoryRepository;
import store.repository.PromotionRepository;

class InventoryServiceTest {

    InventoryService service;
    InventoryRepository inventoryRepository;

    @BeforeEach
    void setUp() {
        inventoryRepository = new InventoryRepository();
        PromotionRepository promotionRepository = new PromotionRepository();

        Promotion promo1 = new Promotion("탄산2+1", 2, 1,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31));
        Promotion promo2 = new Promotion("MD추천상품", 1, 1,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31));
        promotionRepository.saveAll(List.of(promo1, promo2));

        service = new InventoryService(inventoryRepository, promotionRepository);
    }

    @Test
    void 인벤토리를_불러오고_정상적으로_프로모션과_연동된다() throws IOException {
        // given
        String filePath = Paths.get("src", "main", "resources", "products.md").toString();

        // when
        service.loadInventory(filePath);

        // then
        List<Inventory> inventories = inventoryRepository.findAll();
        assertEquals(11, inventories.size());

        Inventory colaInventory = inventories.stream()
                .filter(inv -> inv.getProductName().equals("콜라"))
                .findFirst()
                .orElse(null);

        assertNotNull(colaInventory);
        assertEquals("콜라", colaInventory.getProductName());
    }
}
