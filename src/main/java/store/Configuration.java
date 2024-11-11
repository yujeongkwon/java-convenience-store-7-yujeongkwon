package store;

import store.controller.StoreController;
import store.exception.ExceptionHandler;
import store.inventory.repository.InventoryRepository;
import store.inventory.repository.PromotionRepository;
import store.inventory.service.InventoryService;
import store.inventory.service.PromotionService;
import store.service.StoreService;

public class Configuration {

    private final InventoryRepository inventoryRepository = new InventoryRepository();
    private final PromotionRepository promotionRepository = new PromotionRepository();
    private final PromotionService promotionService = new PromotionService(promotionRepository);
    private final InventoryService inventoryService = new InventoryService(inventoryRepository, promotionRepository);
    private final StoreService storeService = new StoreService(inventoryService, promotionService);
    private final ExceptionHandler exceptionHandler = new ExceptionHandler();

    public StoreController storeController() {
        StoreController storeController = new StoreController(storeService, exceptionHandler);
        storeController.init();
        return storeController;
    }
}