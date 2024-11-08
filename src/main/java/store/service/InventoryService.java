package store.service;

import java.io.IOException;
import java.util.Map;
import store.domain.Inventory;
import store.domain.Promotion;
import store.parser.ProductParser;
import store.repository.InventoryRepository;
import store.repository.PromotionRepository;

public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final PromotionRepository promotionRepository;

    public InventoryService(InventoryRepository inventoryRepository, PromotionRepository promotionRepository) {
        this.inventoryRepository = inventoryRepository;
        this.promotionRepository = promotionRepository;
    }

    public void loadInventory(String filePath) throws IOException {
        Map<Inventory, String> inventoryPromotionMap = ProductParser.parseProducts(filePath);
        inventoryPromotionMap.forEach(this::applyPromotionAndSave);
    }

    private void applyPromotionAndSave(Inventory inventory, String promotionName) {
        Promotion promotion = promotionRepository.findByName(promotionName);
        if (promotion != null) {
            inventory.changePromotion(promotion);
        }

        inventoryRepository.save(inventory);
    }
}
