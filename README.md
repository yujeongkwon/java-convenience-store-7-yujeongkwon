# 🛒 W 편의점 결제 시스템

---

우아한 테크 코스의 4주차 미션인 편의점 입니다. 최대한 **객체끼리의 협력**을 통해서 기능을 구현하고자 하였습니다. ✨

## 💸  W 편의점 결제 시스템 소개

---

W 편의점 결제 시스템은 고객님이 선택한 상품의 가격과 수량을 기반으로 최종 결제 금액을 산출하며, 멤버십 할인과 프로모션 혜택을 통해 더욱 합리적인 가격을 제공합니다. 시스템은 상품별 재고 관리, 프로모션 할인 적용, 멤버십 할인 적용을 모두 포함하고, 모든 내역을 보기 쉽게 영수증으로 안내합니다.

## 🧮 결제 시스템 진행 방식

1.  환영 인사와 함께 상품명, 가격, 프로모션 이름, 재고 상황을 안내드립니다. 만약 재고가 0개라면 `재고 없음` 이 출력됩니다.
- 아래는 예시 출력입니다.

    ```
    안녕하세요. W편의점입니다.
    현재 보유하고 있는 상품입니다.
    
    - 콜라 1,000원 10개 탄산2+1
    - 콜라 1,000원 10개
    - 사이다 1,000원 8개 탄산2+1
    - 사이다 1,000원 7개
    - 오렌지주스 1,800원 9개 MD추천상품
    - 오렌지주스 1,800원 재고 없음
    - 탄산수 1,200원 5개 탄산2+1
    - 탄산수 1,200원 재고 없음
    - 물 500원 10개
    - 비타민워터 1,500원 6개
    - 감자칩 1,500원 5개 반짝할인
    - 감자칩 1,500원 5개
    - 초코바 1,200원 5개 MD추천상품
    - 초코바 1,200원 5개
    - 에너지바 2,000원 5개
    - 정식도시락 6,400원 8개
    - 컵라면 1,700원 1개 MD추천상품
    - 컵라면 1,700원 10개
    ```

2. **재고 목록을 확인하신 후, 상품 가격과 수량을 입력합니다.**
    - 상품명과 수량은 하이픈(-)으로, 개별 상품은 대괄호([])로 묶어 쉼표(,)로 구분합니다.
    - 아래와 같이 잘못 입력 했을 경우 재입력 부탁드립니다.
        - **잘못된 형식**: 형식에 맞지 않는 입력값은 `[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.` 라고 안내됩니다.
        - **존재하지 않는 상품**: 입력한 상품이 재고에 없는 경우 `[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.` 라고 안내됩니다.
        - **재고 초과**: 입력한 수량이 재고보다 많은 경우 `[ERROR] 재고 수량을 초과하여 구매할 수 없습니다.` 라고 안내됩니다.
        - **중복된 상품**: 동일 상품을 중복으로 입력할 경우 `[ERROR] 중복된 상품이 있습니다. 다시 입력해 주세요.` 라고 안내됩니다.
    - 아래는 입출력 예시입니다.

    ```
    구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
    ```


3. **프로모션 추가 수량 선택**

- 고객님께서 프로모션 적용이 가능한 상품에 대해 해당 수량보다 적게 가져온 경우, 그 수량만큼 추가 여부를 입력받습니다.
- 입력은 2가지 입력만 허용됩니다.

    ```
    Y: 증정 받을 수 있는 상품을 추가한다.
    N: 증정 받을 수 있는 상품을 추가하지 않는다.
    ```

- 아래와 같이 잘못 입력 했을 경우 재입력 부탁드립니다.
    - `Y` 또는 `N` 이외의 값을 입력할 경우 `[ERROR] 잘못된 입력입니다. Y 또는 N으로 다시 입력해 주세요.` 라고 안내됩니다.
- 예시:

    ```
    현재 콜라 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)
    Y
    ```

1. **프로모션 재고가 부족으로 인한 일부 수량 정가 결제 여부**
    - 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지 여부를 입력받습니다.
    - 입력은 2가지 입력만 허용됩니다.

        ```
        Y: 일부 수량에 대해 정가로 결제한다.
        N: 정가로 결제해야하는 수량만큼 제외한 후 결제를 진행한다.
        ```

    - 아래와 같이 잘못 입력 했을 경우 재입력 부탁드립니다.
        - `Y` 또는 `N` 이외의 값을 입력할 경우 `[ERROR] 잘못된 입력입니다. Y 또는 N으로 다시 입력해 주세요.` 라고 안내됩니다.
    - 예시:

        ```
        현재 콜라 4개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
        Y
        ```

2.  **멤버십 할인 적용 여부 선택**
- 멤버십 회원인 경우, 프로모션 적용 후 남은 금액의 최대 30% 할인을 받을 수 있습니다. 최대 할인 한도는 8,000원입니다.
- 입력은 2가지 입력만 허용됩니다.

    ```
    Y: 멤버십 할인을 적용한다.
    N: 멤버십 할인을 적용하지 않는다.
    ```

- 아래와 같이 잘못 입력 했을 경우 재입력 부탁드립니다.
    - `Y` 또는 `N` 이외의 값을 입력할 경우 `[ERROR] 잘못된 입력입니다. Y 또는 N으로 다시 입력해 주세요.` 라고 안내됩니다.
- 예시:

    ```
    멤버십 할인을 받으시겠습니까? (Y/N)
    Y
    ```

3. **결제 내역 및 영수증 출력**
    - 고객님의 구매 내역과 할인이 적용된 최종 결제 금액을 영수증 형식으로 안내합니다.
    - 예시 출력:

    ```
    ==============W 편의점================
    상품명         수량      금액
    콜라            3         3,000원
    사이다          2         2,000원
    ============= 증정 ==================
    콜라            1개 무료 증정
    ======================================
    총구매액          5        5,000원
    행사할인                      -1,000원
    멤버십할인                   -1,500원
    내실돈                      2,500원
    ======================================
    
    ```


1. **추가 구매 여부 선택**
    - 추가 구매 또한 고려하실 수 있습니다. Y를 입력하시면 다시 **상품 가격과 수량을 입력하실 수 있습니다.**
    - 입력은 2가지 입력만 허용됩니다.

        ```
        Y: 재고가 업데이트된 상품 목록을 확인 후 추가로 구매를 진행한다.
        N: 구매를 종료한다.
        ```

    - 아래와 같이 잘못 입력 했을 경우 재입력 부탁드립니다.
        - `Y` 또는 `N` 이외의 값을 입력할 경우 `[ERROR] 잘못된 입력입니다. Y 또는 N으로 다시 입력해 주세요.` 라고 안내됩니다.
    - 예시:

        ```
        감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
        Y
        ```

<br>

# 🔧구현 목록

---

### 사용자 입력 관련

- [x]  상품 및 수량 입력 받기
    - [x]  입력된 상품명과 수량의 공백 제거
    - [x]  [예외] null 입력일 경우
    - [x]  [예외] 형식이 맞지 않는 경우
    - [x]  [예외] 동일 상품을 중복으로 입력한 경우
    - [x]  [예외] 수량이 양수가 아닐 경우
- [x]  프로모션 추가 수량 선택 받기
    - [x]  `Y` 또는 `N` 입력 확인
    - [x]  [예외] `Y/N` 이외의 값을 입력한 경우
- [x]  멤버십 할인 적용 여부 입력 받기
    - [x]  `Y` 또는 `N` 입력 확인
    - [x]  [예외] `Y/N` 이외의 값을 입력한 경우
- [x]  추가 구매 여부 입력 받기
    - [x]  `Y` 또는 `N` 입력 확인
    - [x]  [예외] `Y/N` 이외의 값을 입력한 경우

### 결제 및 할인

- [x]  상품별 가격과 수량을 곱하여 총 구매액 계산
    - [x]  모든 상품의 총 금액 계산
- [x]  프로모션 할인 적용
    - [x] 프로모션 적용 가능 여부 확인
      - [x] 오늘 날짜가 프로모션 기간 내 여부 확인
      - [x] N개 구매 시 1개 무료 프로모션 적용 여부 확인
    - [x] 구매 갯수에 따라 프로모션 증정 갯수 구하기
    - [x] 프로모션 재고에서 우선 차감
    - [x] 프로모션 재고가 부족할 경우 확인
        - [x] 일반 재고로 대체
            - [x] [예외] 일반 재고도 부족할 경우
        - [x] 일부 수량에 대한 정가 결제 안내
    - [x] 고객이 해당 프로모션 적용 수량보다 적게 가져온 경우 확인
        - [x] 추가 수량 혜택 안내
- [x]  멤버십 할인 적용
    - [x]  프로모션 미적용 금액에서 할인 후 남은 금액의 최대 30% 할인 적용
    - [x]  할인 한도는 8,000원으로 제한

### 재고 관리
- [x] 행사 목록을 파일 입출력을 통해 불러온다.
- [x] 상품 목록을 파일 입출력을 통해 불러온다.
  - [x] 일반 재고와 프로모션 재고를 함께 관리한다.
  - [x] 상품 프로모션 이름과 행사를 연결 시킨다.
- [x] [예외] 사용자가 원하는 상품이 존재하지 않는 상품일 경우
- [x]  각 상품의 재고를 확인하여 구매 가능 여부 판단
  - [x] 구매가 가능하다면 상품 재고 차감
  - [x] [예외] 구매가 불가능하다면 예외 발생 
- [x]  재고 업데이트
    - [x]  구매 수량만큼 재고 차감하여 최신 재고 상태 유지
- [x] 사용자에게 재고 출력
    - [x]  프로모션 할인이 있다면 <br>
      (상품명, 가격, 프로모션 재고, 프로모션), (상품명, 가격, 일반 재고)으로 출력
    - [x]  프로모션 할인이 없다면 <br>
      (상품명, 가격, 일반 재고)으로 출력

### 영수증 출력

- [x]  구매 내역을 포함한 영수증 생성
    - [x]  구매 상품 내역: 구매한 상품명, 수량, 금액
    - [x]  증정 상품 내역: 프로모션에 따라 무료로 제공된 증정 상품의 목록
    - [x]  금액 정보
        - 총구매액: 구매한 상품의 총 수량과 총 금액
        - 행사할인: 프로모션에 의해 할인된 금액
        - 멤버십할인: 멤버십에 의해 추가로 할인된 금액
        - 내실돈: 최종 결제 금액
    - [x]  최종 결제 금액 계산 및 정렬 출력