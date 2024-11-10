package store.view;

import java.util.List;
import store.inventory.dto.InventoryDto;
import store.inventory.dto.InventoryPairDto;

public class OutputView {

    private static final String NEW_LINE = "\n";
    private static final String NO_STOCK = "재고 없음";
    private static final String UNIT = "개";
    private static final String ITEM_FORMAT = "- %s %,d원 %s %s\n";
    private static final StringBuilder BUFFER = new StringBuilder();

    public static void displayErrorMessage(String message) {
        System.out.println(message);
    }

    public static void displayInventory(List<InventoryPairDto> inventoryPairDtos) {
        BUFFER.append("안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n\n");
        inventoryPairDtos.forEach(OutputView::displayInventoryPair);
        BUFFER.append(NEW_LINE);
        clearBuffer();
    }

    private static void displayInventoryPair(InventoryPairDto inventoryPairDto) {
        InventoryDto promotionDto = inventoryPairDto.promotionStockDto();
        InventoryDto generalDto = inventoryPairDto.generalStockDto();

        if (promotionDto != null) {
            appendFormattedItem(promotionDto, formatStockInfo(promotionDto.stock()));
        }

        appendFormattedItem(generalDto, formatStockInfo(generalDto.stock()));
    }

    private static String formatStockInfo(int stock) {
        if (stock <= 0) {
            return NO_STOCK;
        }
        return stock + UNIT;
    }

    private static void appendFormattedItem(InventoryDto dto, String stockInfo) {
        String promotionName = getPromotionName(dto.promotionName());
        BUFFER.append(String.format(ITEM_FORMAT, dto.productName(), dto.price(), stockInfo, promotionName));
    }

    private static String getPromotionName(String promotionName) {
        if (promotionName == null) {
            return "";
        }
        return promotionName;
    }

    private static void clearBuffer() {
        System.out.print(BUFFER);
        BUFFER.setLength(0);
    }
}
