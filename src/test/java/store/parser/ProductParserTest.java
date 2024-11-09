package store.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Test;
import store.dto.InventoryDto;

class ProductParserTest {

    @Test
    void shouldParseProductsCorrectly() throws IOException {
        // given
        String filePath = Paths.get("src", "main", "resources", "products.md").toString();

        // when
        List<InventoryDto> dtos = ProductParser.parseProducts(filePath);

        // then
        assertThat(dtos).hasSize(16);

        InventoryDto colaInventory = dtos.stream()
                .filter(inv -> inv.productName().equals("콜라"))
                .findFirst()
                .orElse(null);
        assertThat(colaInventory.promotionName()).isEqualTo("탄산2+1");
    }
}