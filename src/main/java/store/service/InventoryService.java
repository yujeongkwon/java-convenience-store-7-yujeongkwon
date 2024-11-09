package store.service;

import java.io.IOException;
import java.util.List;
import store.domain.Inventory;
import store.domain.Promotion;
import store.dto.InventoryDto;
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
        List<InventoryDto> dtos = ProductParser.parseProducts(filePath);
        dtos.forEach(this::processInventoryDto);
    }

    private void processInventoryDto(InventoryDto dto) {
        Inventory existingInventory = inventoryRepository.findByProductName(dto.productName()).orElse(null);
        if (existingInventory != null) {
            updateExistingInventory(existingInventory, dto);
            return;
        }
        createAndSaveNewInventory(dto);
    }

    private void updateExistingInventory(Inventory inventory, InventoryDto dto) {
        if (dto.promotionName() == null) {
            inventory.addGeneralStock(dto.stock());
            return;
        }

        inventory.addPromotionStock(dto.stock());
        inventory.changePromotion(findPromotion(dto.promotionName()));
    }

    private void createAndSaveNewInventory(InventoryDto dto) {
        Promotion promotion = findPromotion(dto.promotionName());
        Inventory newInventory = new Inventory(dto.toProduct(promotion), dto.toStock());
        inventoryRepository.save(newInventory);
    }

    private Promotion findPromotion(String promotionName) {
        if (promotionName == null) {
            return null;
        }
        return promotionRepository.findByName(promotionName).orElse(null);
    }
}
