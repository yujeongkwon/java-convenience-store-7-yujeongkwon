package store.inventory.service;

import static store.exception.messages.ErrorMessage.NOT_FOUND;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import store.inventory.domain.InventoryItem;
import store.inventory.domain.Promotion;
import store.inventory.dto.InventoryDto;
import store.inventory.dto.InventoryPairDto;
import store.inventory.parser.ProductParser;
import store.inventory.repository.InventoryRepository;
import store.inventory.repository.PromotionRepository;

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

    public List<InventoryItem> findInventoryItems(List<String> productNames) {
        return productNames.stream()
                .map(name -> inventoryRepository.findByProductName(name)
                        .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND.getMessage())))
                .collect(Collectors.toList());
    }

    public List<InventoryPairDto> getAllInventoryDtos() {
        return inventoryRepository.findAll().stream().map(InventoryPairDto::fromInventory).collect(Collectors.toList());
    }

    private void processInventoryDto(InventoryDto dto) {
        InventoryItem existingInventoryItem = inventoryRepository.findByProductName(dto.productName()).orElse(null);
        if (existingInventoryItem != null) {
            updateExistingInventory(existingInventoryItem, dto);
            return;
        }
        createAndSaveNewInventory(dto);
    }

    private void updateExistingInventory(InventoryItem inventoryItem, InventoryDto dto) {
        if (dto.promotionName() == null) {
            inventoryItem.addGeneralStock(dto.stock());
            return;
        }

        inventoryItem.addPromotionStock(dto.stock());
        inventoryItem.changePromotion(findPromotion(dto.promotionName()));
    }

    private void createAndSaveNewInventory(InventoryDto dto) {
        Promotion promotion = findPromotion(dto.promotionName());
        InventoryItem newInventoryItem = new InventoryItem(dto.toProduct(), dto.toStock(), promotion);
        inventoryRepository.save(newInventoryItem);
    }

    private Promotion findPromotion(String promotionName) {
        if (promotionName == null) {
            return null;
        }
        return promotionRepository.findByName(promotionName).orElse(null);
    }
}
