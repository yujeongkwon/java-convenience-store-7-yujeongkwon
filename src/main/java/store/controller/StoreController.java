package store.controller;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import store.dto.InventoryPairDto;
import store.exception.ExceptionHandler;
import store.service.InventoryService;
import store.service.PromotionService;
import store.view.OutputView;

public class StoreController {

    private static final String PROMOTION_FILE_PATH = Paths.get("src", "main", "resources", "promotions.md").toString();
    private static final String PRODUCT_FILE_PATH = Paths.get("src", "main", "resources", "products.md").toString();

    private final InventoryService inventoryService;
    private final PromotionService promotionService;
    private final ExceptionHandler exceptionHandler;

    public StoreController(InventoryService inventoryService, PromotionService promotionService,
            ExceptionHandler exceptionHandler) {
        this.inventoryService = inventoryService;
        this.promotionService = promotionService;
        this.exceptionHandler = exceptionHandler;
    }

    public void init() {
        exceptionHandler.handleVoidWithIOException(this::loadInitialData);
    }

    public void run() {
        List<InventoryPairDto> inventoryPairDtos = inventoryService.getAllInventoryDtos();
        OutputView.displayInventory(inventoryPairDtos);
    }

    private void loadInitialData() throws IOException {
        promotionService.loadPromotions(PROMOTION_FILE_PATH);
        inventoryService.loadInventory(PRODUCT_FILE_PATH);
    }
}
