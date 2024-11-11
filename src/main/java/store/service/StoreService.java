package store.service;

import static store.order.validator.CartValidator.validateDuplicate;

import camp.nextstep.edu.missionutils.DateTimes;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import store.inventory.dto.InventoryPairDto;
import store.inventory.service.InventoryService;
import store.inventory.service.PromotionService;
import store.order.domain.Cart;
import store.order.dto.OrderItemDto;

public class StoreService {

    private static final String ITEM_DELIMITER = ",";

    private final InventoryService inventoryService;
    private final PromotionService promotionService;

    public StoreService(final InventoryService inventoryService, final PromotionService promotionService) {
        this.inventoryService = inventoryService;
        this.promotionService = promotionService;
    }

    public void loadInitialData(final String promotionFileUrl, final String productFileUrl) throws IOException {
        promotionService.loadPromotions(promotionFileUrl);
        inventoryService.loadInventory(productFileUrl);
    }

    public List<InventoryPairDto> getAllInventoryPairs() {
        return inventoryService.getAllInventoryDtos();
    }

    public Cart prepareCart(final String orderInput) {
        List<OrderItemDto> orderItems = parseOrderItems(orderInput);

        validateDuplicate(orderItems);

        List<String> names = orderItems.stream().map(OrderItemDto::productName).toList();
        return Cart.of(orderItems, inventoryService.findInventoryItems(names), LocalDate.from(DateTimes.now()));
    }

    private List<OrderItemDto> parseOrderItems(final String input) {
        return Stream.of(input.split(ITEM_DELIMITER)).map(OrderItemDto::from).collect(Collectors.toList());
    }
}
