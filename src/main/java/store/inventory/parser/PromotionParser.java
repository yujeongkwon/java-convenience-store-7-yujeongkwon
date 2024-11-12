package store.inventory.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import store.inventory.domain.Promotion;

public class PromotionParser {

    private static final int HEADER_LINE = 1;
    private static final String DELIMITER = ",";

    public static List<Promotion> parsePromotions(String filePath) throws IOException {
        List<Promotion> promotions = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            reader.lines()
                    .skip(HEADER_LINE)
                    .filter(line -> !line.isBlank())
                    .forEach(line -> promotions.add(parsePromotion(line)));
        }
        return promotions;
    }

    private static Promotion parsePromotion(String line) {
        String[] data = line.split(DELIMITER);
        String name = data[0].trim();
        int buyQuantity = Integer.parseInt(data[1].trim());
        int freeQuantity = Integer.parseInt(data[2].trim());
        LocalDate startDate = LocalDate.parse(data[3].trim());
        LocalDate endDate = LocalDate.parse(data[4].trim());

        return new Promotion(name, buyQuantity, freeQuantity, startDate, endDate);
    }
}