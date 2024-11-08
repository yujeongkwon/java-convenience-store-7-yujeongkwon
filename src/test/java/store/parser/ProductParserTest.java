package store.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import org.junit.jupiter.api.Test;
import store.domain.Inventory;

class ProductParserTest {

    @Test
    void shouldParseProductsCorrectly() throws IOException {
        // given
        String filePath = Paths.get("src", "main", "resources", "products.md").toString();

        // when
        Map<Inventory, String> inventoryPromotionMap = ProductParser.parseProducts(filePath);

        // then
        assertThat(inventoryPromotionMap).hasSize(11);

        Inventory colaInventory = inventoryPromotionMap.keySet().stream()
                .filter(inv -> inv.getProductName().equals("콜라")).findFirst().orElse(null);
        assertThat(colaInventory).isNotNull();
        assertThat(inventoryPromotionMap.get(colaInventory)).isEqualTo("탄산2+1");
    }
}