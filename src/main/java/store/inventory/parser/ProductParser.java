package store.inventory.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import store.inventory.dto.InventoryDto;

public class ProductParser {

    private static final String NULL_PROMOTION = "null";
    private static final int HEADER_LINE = 1;
    private static final String DELIMITER = ",";

    public static List<InventoryDto> parseProducts(String filePath) throws IOException {
        List<InventoryDto> inventoryDtos = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            reader.lines()
                    .skip(HEADER_LINE)
                    .filter(line -> !line.isBlank())
                    .forEach(line -> inventoryDtos.add(parseToDto(line)));
        }
        return inventoryDtos;
    }

    private static InventoryDto parseToDto(String line) {
        String[] data = line.split(DELIMITER);
        String name = data[0].trim();
        int price = Integer.parseInt(data[1].trim());
        int quantity = Integer.parseInt(data[2].trim());
        String promotionName = getPromotionName(data[3].trim());

        return new InventoryDto(name, price, quantity, promotionName);
    }

    private static String getPromotionName(String promotion) {
        if (promotion.equals(NULL_PROMOTION)) {
            return null;
        }
        return promotion;
    }
}
