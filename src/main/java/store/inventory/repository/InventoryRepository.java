package store.inventory.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import store.inventory.domain.InventoryItem;

public class InventoryRepository {

    private final List<InventoryItem> inventories = new ArrayList<>();

    public void save(InventoryItem inventoryItem) {
        inventories.add(inventoryItem);
    }

    public List<InventoryItem> findAll() {
        return new ArrayList<>(inventories);
    }

    public Optional<InventoryItem> findByProductName(String productName) {
        return inventories.stream()
                .filter(inventory -> inventory.getProductName().equals(productName))
                .findFirst();
    }
}
