package store.service;

import java.io.IOException;
import java.util.List;
import store.domain.Promotion;
import store.parser.PromotionParser;
import store.repository.PromotionRepository;

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
