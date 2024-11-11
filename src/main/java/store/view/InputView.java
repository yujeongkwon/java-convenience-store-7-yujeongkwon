package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {

    public static String readOrder() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        return Console.readLine();
    }

    public static String readRetryStatus() {
        return askAboutPromotion("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
    }

    public static String readMembershipStatus() {
        return askAboutPromotion("\n멤버십 할인을 받으시겠습니까? (Y/N)");
    }

    public static String askAboutPromotion(String message) {
        System.out.println(message);
        return Console.readLine();
    }
}
