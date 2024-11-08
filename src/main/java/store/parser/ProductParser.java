package store.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import store.domain.Inventory;
import store.domain.Product;
import store.domain.Stock;

public class ProductParser {

    private static final String NULL_PROMOTION = "null";
    private static final int HEADER_LINE = 1;
    private static final String DELIMITER = ",";

    private static Map<Inventory, String> inventoryPromotionMap;

    public static Map<Inventory, String> parseProducts(String filePath) throws IOException {
        inventoryPromotionMap = new HashMap<>();

        Files.lines(Paths.get(filePath))
                .skip(HEADER_LINE)
                .filter(line -> !line.isBlank())
                .forEach(ProductParser::parse);
        return inventoryPromotionMap;
    }

    private static void parse(String line) {
        String[] data = line.split(DELIMITER);
        String name = data[0].trim();
        int price = Integer.parseInt(data[1].trim());
        int quantity = Integer.parseInt(data[2].trim());
        String promotionName = getPromotionName(data[3].trim());

        addOrUpdateInventory(name, quantity, promotionName, price);
    }

    private static String getPromotionName(String promotion) {
        if (promotion.equals(NULL_PROMOTION)) {
            return null;
        }
        return promotion;
    }

    private static void addOrUpdateInventory(String name, int quantity, String promotionName, int price) {
        Inventory existingInventory = findInventoryByName(name);
        if (existingInventory != null) {
            existingInventory.addGeneralStock(quantity);
            return;
        }

        Product product = new Product(name, price, null);
        Stock stock = createStock(quantity, promotionName);
        inventoryPromotionMap.put(new Inventory(product, stock), promotionName);
    }

    private static Inventory findInventoryByName(String name) {
        return inventoryPromotionMap.keySet()
                .stream()
                .filter(inventory -> inventory.getProductName().equals(name))
                .findFirst().orElse(null);
    }

    private static Stock createStock(int quantity, String promotionName) {
        if (promotionName == null) {
            return new Stock(quantity, 0);
        }
        return new Stock(0, quantity);
    }
}
