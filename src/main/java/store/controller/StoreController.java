package store.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import store.exception.ExceptionHandler;
import store.exception.ExceptionResponse;
import store.exception.UserDecisionException;
import store.inventory.dto.InventoryPairDto;
import store.inventory.service.InventoryService;
import store.inventory.service.PromotionService;
import store.order.domain.Cart;
import store.order.domain.CartItem;
import store.order.domain.PaymentCalculator;
import store.order.dto.OrderItemDto;
import store.order.dto.ReceiptDto;
import store.order.dto.YesOrNoDto;
import store.view.InputView;
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
        do {
            displayInventory();
            Cart cart = createCartWithRetry();
            applyPromotions(cart);
            PaymentCalculator calculator = askMembershipStatus(cart);
            displayReceipt(calculator);
        } while(continueShoppingStatus());
    }

    private void loadInitialData() throws IOException {
        promotionService.loadPromotions(PROMOTION_FILE_PATH);
        inventoryService.loadInventory(PRODUCT_FILE_PATH);
    }

    private void displayInventory() {
        List<InventoryPairDto> inventoryPairDtos = inventoryService.getAllInventoryDtos();
        OutputView.displayInventory(inventoryPairDtos);
    }

    private Cart createCartWithRetry() {
        return exceptionHandler.handleWithRetry(this::createCart).result();
    }

    private void applyPromotions(Cart cart) {
        cart.getItems().forEach(this::applyPromotionWithRetry);
    }

    private PaymentCalculator askMembershipStatus(Cart cart) {
        boolean membershipStatus = exceptionHandler.handleWithRetry(() -> YesOrNoDto.from(InputView.readMembershipStatus()))
                .result()
                .isYesOrNo();
        return cart.createPaymentCalculator(membershipStatus);
    }

    private boolean continueShoppingStatus() {
        return exceptionHandler.handleWithRetry(() -> YesOrNoDto.from(InputView.readRetryStatus()))
                .result()
                .isYesOrNo();
    }

    private Cart createCart() {
        List<OrderItemDto> orderItems = parseOrderItems(InputView.readOrder());
        List<String> names = orderItems.stream().map(OrderItemDto::productName).toList();
        return Cart.of(orderItems, inventoryService.findInventoryItems(names), LocalDate.from(DateTimes.now()));
    }

    private List<OrderItemDto> parseOrderItems(String input) {
        return Stream.of(input.split(",")).map(OrderItemDto::from).collect(Collectors.toList());
    }
    
    private void applyPromotionWithRetry(CartItem cartItem) {
        ExceptionResponse<Void> response = exceptionHandler.handleWithRetry(() -> {
            cartItem.drainStock(LocalDate.from(DateTimes.now()));
            return null;
        });
        if (response.hasException()) {
            handleUserDecision(response.userDecisionException(), cartItem);
        }
    }

    private void handleUserDecision(UserDecisionException exception, CartItem cartItem) {
        if (exception.isPromotionNotAvailableException()) {
            cartItem.handlePromotionShortage(exception.getUserChoice(), exception.getQuantity());
        }

        if (exception.isAdditionalBenefitException() && exception.getUserChoice()) {
            cartItem.addEligibleFreeItems(exception.getQuantity());
        }
    }

    private void displayReceipt(PaymentCalculator calculator) {
        ReceiptDto receipt = calculator.generateReceipt();
        OutputView.displayReceipt(receipt);
    }

}
