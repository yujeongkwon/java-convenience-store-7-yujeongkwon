package store.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.function.Supplier;
import store.exception.ExceptionHandler;
import store.exception.ExceptionResponse;
import store.exception.UserDecisionException;
import store.order.domain.Cart;
import store.order.domain.CartItem;
import store.order.domain.PaymentCalculator;
import store.order.dto.ReceiptDto;
import store.order.dto.YesOrNoDto;
import store.service.StoreService;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {

    private static final String PROMOTION_FILE_PATH = Paths.get("src", "main", "resources", "promotions.md").toString();
    private static final String PRODUCT_FILE_PATH = Paths.get("src", "main", "resources", "products.md").toString();

    private final StoreService storeService;
    private final ExceptionHandler exceptionHandler;

    public StoreController(StoreService storeService, ExceptionHandler exceptionHandler) {
        this.storeService = storeService;
        this.exceptionHandler = exceptionHandler;
    }

    public void init() {
        exceptionHandler.handleVoidWithIOException(
                () -> storeService.loadInitialData(PROMOTION_FILE_PATH, PRODUCT_FILE_PATH));
    }

    public void run() {
        do {
            displayInventory();
            final Cart cart = createCartWithRetry();
            processCart(cart);
        } while (continueShopping());
    }

    private Cart createCartWithRetry() {
        return exceptionHandler.handleWithRetry(() -> storeService.prepareCart(InputView.readOrder())).result();
    }

    private void displayInventory() {
        OutputView.displayInventory(storeService.getAllInventoryPairs());
    }

    private void processCart(final Cart cart) {
        applyPromotions(cart);
        final PaymentCalculator calculator = requestMembershipStatus(cart);
        displayReceipt(calculator);
    }

    private boolean continueShopping() {
        return handleYesOrNoRetry(InputView::readRetryStatus);
    }

    private PaymentCalculator requestMembershipStatus(final Cart cart) {
        final boolean membershipStatus = handleYesOrNoRetry(InputView::readMembershipStatus);
        return cart.createPaymentCalculator(membershipStatus);
    }

    private boolean handleYesOrNoRetry(final Supplier<String> inputSupplier) {
        return exceptionHandler.handleWithRetry(() -> YesOrNoDto.from(inputSupplier.get())).result().isYesOrNo();
    }

    private void applyPromotions(final Cart cart) {
        cart.getItems().forEach(this::applyPromotionWithRetry);
    }

    private void displayReceipt(final PaymentCalculator calculator) {
        final ReceiptDto receipt = calculator.generateReceipt();
        OutputView.displayReceipt(receipt);
    }

    private void applyPromotionWithRetry(final CartItem cartItem) {
        ExceptionResponse<Void> response = exceptionHandler.handleWithRetry(() -> {
            cartItem.drainStock(LocalDate.from(DateTimes.now()));
            return null;
        });
        if (response.hasException()) {
            processUserDecision(response.userDecisionException(), cartItem);
        }
    }

    private void processUserDecision(final UserDecisionException exception, final CartItem cartItem) {
        if (exception.isPromotionNotAvailableException()) {
            cartItem.handlePromotionShortage(exception.getUserChoice(), exception.getQuantity());
            return;
        }

        if (exception.isAdditionalBenefitException() && exception.getUserChoice()) {
            cartItem.addEligibleFreeItems(exception.getQuantity());
        }
    }
}
