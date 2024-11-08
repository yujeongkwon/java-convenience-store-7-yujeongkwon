package store.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import store.domain.Promotion;

public class PromotionParser {

    private static final int HEADER_LINE = 1;
    public static final String DELIMITER = ",";

    public static List<Promotion> parsePromotions(String filePath) throws IOException {
        return Files.lines(Paths.get(filePath))
                .skip(HEADER_LINE)
                .filter(line -> !line.isBlank())
                .map(PromotionParser::parsePromotion)
                .collect(Collectors.toList());
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