package store.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import store.domain.Inventory;

public class InventoryRepository {

    private final List<Inventory> inventories = new ArrayList<>();

    public void save(Inventory inventory) {
        inventories.add(inventory);
    }

    public List<Inventory> findAll() {
        return new ArrayList<>(inventories);
    }

    public Optional<Inventory> findByProductName(String productName) {
        return inventories.stream()
                .filter(inventory -> inventory.getProductName().equals(productName))
                .findFirst();
    }
}
