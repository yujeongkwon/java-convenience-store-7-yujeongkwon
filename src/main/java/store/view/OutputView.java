package store.view;

import java.util.List;
import store.inventory.dto.InventoryDto;
import store.inventory.dto.InventoryPairDto;
import store.order.dto.FreeItemDto;
import store.order.dto.ReceiptDto;
import store.order.dto.ReceiptItemDto;

public class OutputView {

    private static final String NEW_LINE = "\n";
    private static final String NO_STOCK = "재고 없음";
    private static final String UNIT = "개";
    private static final String ITEM_FORMAT = "- %s %,d원 %s %s\n";
    private static final String PURCHASED_ITEM_FORMAT = "%-10s %5d %10s%n";
    private static final String FREE_ITEM_FORMAT = "%-10s %9d개%n";
    private static final String SUMMARY_FORMAT = "%-10s %10s%n";
    private static final String RECEIPT_HEADER = "==============W 편의점================";
    private static final String PURCHASED_HEADER = "상품명           수량       금액";
    private static final String FREE_HEADER = "=============증 정===============";
    private static final String SUMMARY_HEADER = "=====================================";

    private static final StringBuilder BUFFER = new StringBuilder();

    public static void displayErrorMessage(final String message) {
        System.out.println(message);
    }

    public static void displayInventory(List<InventoryPairDto> inventoryPairDtos) {
        BUFFER.append(NEW_LINE).append("안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n\n");
        inventoryPairDtos.forEach(OutputView::displayInventoryPair);
        BUFFER.append(NEW_LINE);
        clearBuffer();
    }

    public static void displayReceipt(ReceiptDto receipt) {
        BUFFER.append(NEW_LINE).append(RECEIPT_HEADER).append(NEW_LINE);
        appendPurchasedItems(receipt.purchasedItems());
        appendFreeItems(receipt.freeItems());
        appendSummary(receipt);
        clearBuffer();
    }

    private static void clearBuffer() {
        System.out.print(BUFFER);
        BUFFER.setLength(0);
    }

    private static void displayInventoryPair(InventoryPairDto inventoryPairDto) {
        InventoryDto promotionDto = inventoryPairDto.promotionStockDto();
        InventoryDto generalDto = inventoryPairDto.generalStockDto();

        if (promotionDto != null) {
            appendFormattedItem(promotionDto, formatStockInfo(promotionDto.stock()));
        }

        appendFormattedItem(generalDto, formatStockInfo(generalDto.stock()));
    }

    private static String formatStockInfo(final int stock) {
        if (stock <= 0) {
            return NO_STOCK;
        }
        return stock + UNIT;
    }

    private static void appendFormattedItem(InventoryDto dto, String stockInfo) {
        String promotionName = getPromotionName(dto.promotionName());
        BUFFER.append(String.format(ITEM_FORMAT, dto.productName(), dto.price(), stockInfo, promotionName));
    }

    private static String getPromotionName(final String promotionName) {
        if (promotionName == null) {
            return "";
        }
        return promotionName;
    }

    private static void appendPurchasedItems(List<ReceiptItemDto> purchasedItems) {
        BUFFER.append(PURCHASED_HEADER).append(NEW_LINE);
        purchasedItems.forEach(item -> BUFFER.append(formatPurchasedItem(item)));
    }

    private static String formatPurchasedItem(ReceiptItemDto item) {
        return String.format(PURCHASED_ITEM_FORMAT, item.productName(), item.quantity(),
                String.format("%,d원", item.price()));
    }

    private static void appendFreeItems(List<FreeItemDto> freeItems) {
        BUFFER.append(FREE_HEADER).append(NEW_LINE).append(String.format(SUMMARY_FORMAT, "상품", "수량"));
        freeItems.forEach(item -> BUFFER.append(formatFreeItem(item)));
    }

    private static String formatFreeItem(FreeItemDto freeItem) {
        return String.format(FREE_ITEM_FORMAT, freeItem.productName(), freeItem.quantity());
    }

    private static void appendSummary(ReceiptDto receipt) {
        BUFFER.append(SUMMARY_HEADER).append(NEW_LINE)
                .append(formatSummaryLine("총구매액", String.format("%,d원", receipt.totalAmount())))
                .append(formatSummaryLine("행사할인", String.format("%,d원", -receipt.promotionDiscount())))
                .append(formatSummaryLine("멤버십할인", String.format("%,d원", -receipt.membershipDiscount())))
                .append(formatSummaryLine("내실돈", String.format("%,d원", receipt.finalAmount()))).append(SUMMARY_HEADER)
                .append(NEW_LINE).append(NEW_LINE);
    }

    private static String formatSummaryLine(final String label, final String amount) {
        return String.format(SUMMARY_FORMAT, label, amount);
    }
}
