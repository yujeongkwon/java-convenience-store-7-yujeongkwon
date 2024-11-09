package store.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Test;
import store.domain.Promotion;

class PromotionParserTest {

    @Test
    void 행사_목록을_파일_입출력을_통해_불러온다() throws IOException {
        // given
        String filePath = Paths.get("src", "main", "resources", "promotions.md").toString();

        // when
        List<Promotion> promotions = PromotionParser.parsePromotions(filePath);

        // then
        assertThat(promotions).hasSize(3);
        assertThat(promotions).extracting("name")
                .containsExactly("탄산2+1", "MD추천상품", "반짝할인");
    }
}