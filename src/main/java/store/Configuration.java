package store;

import store.controller.StoreController;
import store.exception.ExceptionHandler;
import store.inventory.repository.InventoryRepository;
import store.inventory.repository.PromotionRepository;
import store.inventory.service.InventoryService;
import store.inventory.service.PromotionService;

public class Configuration {

    private final InventoryRepository inventoryRepository = new InventoryRepository();
    private final PromotionRepository promotionRepository = new PromotionRepository();
    private final PromotionService promotionService = new PromotionService(promotionRepository);
    private final InventoryService inventoryService = new InventoryService(inventoryRepository, promotionRepository);
    private final ExceptionHandler exceptionHandler = new ExceptionHandler();

    public StoreController storeController() {
        StoreController storeController = new StoreController(inventoryService, promotionService, exceptionHandler);
        storeController.init();
        return storeController;
    }
}