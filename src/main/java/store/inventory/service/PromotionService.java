package store.inventory.service;

import java.io.IOException;
import java.util.List;
import store.inventory.domain.Promotion;
import store.inventory.parser.PromotionParser;
import store.inventory.repository.PromotionRepository;

public class PromotionService {

    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public void loadPromotions(String filePath) throws IOException {
        List<Promotion> promotions = PromotionParser.parsePromotions(filePath);
        promotionRepository.saveAll(promotions);
    }
}
