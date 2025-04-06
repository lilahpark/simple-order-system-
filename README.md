# 🛒 쇼핑몰 주문/재고 관리 시스템

Spring Boot 기반의 쇼핑몰 주문/재고 관리 시스템입니다. 
주문 생성 시 재고를 실시간으로 확인하고, 캐시를 이용해 성능을 향상시켰습니다.

---

## 🧩 문제 정의

- **실시간 재고 확인**이 필요한 주문 시스템
- 재고 변동이 잦아 **DB 트래픽**이 증가하고, **성능 저하** 발생
- 동시에 여러 사용자가 접근할 경우 **데이터 무결성 문제** 발생 가능

---

## ⚙️ 사용한 기술 & 해결 방식

| 카테고리 | 기술 | 사용 이유 |
|----------|------|------------|
| Backend | Spring Boot, JPA, Spring MVC | 빠른 개발과 안정성 확보 |
| Infra   | Redis, Docker | 캐시 및 컨테이너 환경 설정 |
| View    | Thymeleaf, JavaScript | 간단한 UI 구현 및 동적 반응 |
| DB      | H2 (개발용) | 인메모리 테스트 및 간편한 실행 |

- **Redis**: 재고 정보를 캐싱하여 DB 부하를 줄이고, TTL(24시간)을 설정해 데이터 갱신
- **Cache Aside Pattern**: 캐시에 없으면 DB에서 조회 후 저장
- **재고 수준별 스타일링**: JavaScript로 UI에 즉시 반영해 사용자 경험 개선
- **재고 검증 로직**: 주문 요청 전후 이중 체크로 무결성 유지

---

## 🧰 기술 스택

```
Spring Boot, Spring MVC, JPA, Thymeleaf, Redis, Docker, JavaScript
```

---

## 📁 프로젝트 구조

<pre><code>📦 src ┗ 📂 main ┗ 📂 java ┗ 📂 jpabook ┗ 📂 jpashop ┣ 📂 controller # 웹 요청 처리 (Spring MVC 컨트롤러) ┃ ┣ 📜 BookForm.java # 도서 등록 폼 DTO ┃ ┣ 📜 HomeController.java # 홈 화면 라우팅 ┃ ┣ 📜 ItemController.java # 상품 관련 요청 처리 ┃ ┣ 📜 MemberController.java # 회원 등록 및 조회 ┃ ┣ 📜 OrderController.java # 주문 등록/조회/취소 처리 ┃ ┣ 📜 RedisTestController.java# Redis 테스트용 컨트롤러 ┃ ┗ 📜 StockController.java # 재고 관리 처리 ┣ 📂 domain # 핵심 도메인 모델 (JPA 엔티티) ┃ ┣ 📂 item ┃ ┃ ┣ 📜 Address.java # 배송지 정보 Value Object ┃ ┃ ┣ 📜 Category.java # 상품 분류 ┃ ┃ ┣ 📜 Delivery.java # 배송 엔티티 ┃ ┃ ┣ 📜 DeliveryStatus.java # 배송 상태 (ENUM) ┃ ┃ ┣ 📜 Member.java # 회원 엔티티 ┃ ┃ ┣ 📜 Order.java # 주문 엔티티 (주문자, 상태 등 포함) ┃ ┃ ┣ 📜 OrderItem.java # 주문 상품 상세 (상품, 수량, 가격 등) ┃ ┃ ┗ 📜 OrderStatus.java # 주문 상태 (ENUM) ┣ 📂 exception # 커스텀 예외 정의 ┃ ┗ 📜 NotEnoughStockException.java # 재고 부족 시 발생 예외 ┣ 📂 repository # 데이터베이스 접근 계층 ┃ ┣ 📜 ItemRepository.java # 상품 관련 쿼리 처리 ┃ ┣ 📜 MemberRepository.java # 회원 관련 쿼리 처리 ┃ ┣ 📜 OrderRepository.java # 주문 관련 쿼리 처리 ┃ ┗ 📜 OrderSearch.java # 주문 검색 조건 DTO ┣ 📂 service # 핵심 비즈니스 로직 처리 ┃ ┣ 📜 HelloController.java # 단순 테스트용 컨트롤러 ┃ ┣ 📜 ItemService.java # 상품 등록/수정 비즈니스 로직 ┃ ┣ 📜 MemberService.java # 회원 등록 비즈니스 로직 ┃ ┣ 📜 OrderService.java # 주문 생성/취소 로직 ┃ ┗ 📜 StockService.java # 재고 증가/감소 처리 ┗ 📜 JpashopApplication.java # Spring Boot 애플리케이션 실행 클래스 </code></pre>

```

---

## 📌 주요 API 명세

| Method | URL | 설명 |
|--------|-----|------|
| `GET`  | `/api/items/{itemId}/stock` | 특정 상품의 재고 수량 조회 |
| `POST` | `/order` | 주문 생성 (수량 유효성 검증 포함) |

---


## 📝 기타

- 주문 수량 유효성 체크는 **프론트/백엔드 이중 검증**으로 안정성을 높였습니다.
- Redis TTL을 24시간으로 설정하여 **데이터 갱신 주기**를 확보했습니다.

---

## 🙋 About Me

서비스와 솔루션 아키텍처에 관심이 많으며,  
설계와 협업, 유연한 소통을 중시하는 백엔드 개발자입니다.  
문제를 분석하고 도메인 중심으로 안정적인 시스템을 구축하는 것을 좋아합니다.

---

## 🔗 외부 링크

- [기술 블로그]- https://lilapark.tistory.com/
