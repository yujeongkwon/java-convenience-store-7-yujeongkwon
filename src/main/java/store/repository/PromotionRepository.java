package store.repository;

import java.util.ArrayList;
import java.util.List;
import store.domain.Promotion;

public class PromotionRepository {
    private final List<Promotion> promotions = new ArrayList<>();

    public void saveAll(List<Promotion> promotions) {
        this.promotions.addAll(promotions);
    }

    public List<Promotion> findAll() {
        return new ArrayList<>(promotions);
    }
}