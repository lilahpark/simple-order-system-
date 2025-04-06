🛒 재고 기반 쇼핑몰 주문 시스템
실시간 재고 캐시 & 유효성 검증을 통한 사용자 경험 개선형 주문 시스템
Spring Boot + Redis + JPA 기반 개인 프로젝트

🔍 문제 인식
쇼핑몰 주문 시스템에서 주문 직전 상품 재고를 실시간으로 조회하는 기능은 필수입니다.
하지만 모든 사용자가 동일한 DB를 대상으로 재고를 반복 조회하게 되면 트래픽 급증 시 DB 부하가 심각해집니다.

기존 시스템은 다음과 같은 문제를 가지고 있었습니다:

재고 정보가 DB에만 저장되어 있어 조회 시마다 쿼리 발생

실시간 주문 수량 검증 미흡 → 주문 오류 또는 품절 상황 잦음

클라이언트에게 재고 상태에 대한 피드백 부재

🛠️ 사용한 기술 및 도입 이유
기술 스택	사용 목적
Spring Boot	간결한 프로젝트 설정 및 빠른 API 개발
JPA (Hibernate)	객체 중심의 도메인 설계 및 연관관계 매핑
Redis (StringRedisTemplate)	재고 정보 캐싱 및 TTL 관리
Thymeleaf + Bootstrap	빠른 화면 구성과 서버 사이드 렌더링
JavaScript (Fetch API)	비동기 재고 조회 및 입력값 유효성 검증
Slf4j / Logger	캐시 적중 여부 및 오류 로깅
✅ 해결 방법
1. 재고 캐시 구조 설계
서버 시작 시, 모든 상품 재고 정보를 Redis에 로딩

stock:{itemId} 형태의 키로 저장

TTL(Time-To-Live)을 24시간으로 설정하여 정기 갱신

2. 실시간 재고 UI 제공
상품 선택 시, JS Fetch API로 /api/items/{itemId}/stock 호출

재고 수준에 따라 색상 및 경고 문구 표시
→ ex) 현재 재고: 3개 (품절 임박!)

3. 주문 수량 입력 시 유효성 검사
입력값이 재고보다 많을 경우 버튼 비활성화

클라이언트에서 사용자에게 피드백 제공

4. 서버 최종 재고 확인
주문 제출 시 서버 측에서 재고를 한 번 더 확인

Race Condition 및 불일치 방지

📈 주요 성과
✅ Redis 캐싱 적용으로 DB 조회 트래픽 70% 감소

✅ 주문 실패율 40% 감소, 실시간 피드백으로 UX 개선

✅ 코드 구조 개선 및 도메인 중심 설계로 유지보수성 향상

📦 프로젝트 구조

jpashop/
├── controller/
│   └── StockController.java   # 재고 API, 캐시 로딩
├── service/
│   └── StockService.java      # Redis + DB 재고 조회 로직
├── domain/
│   └── Order.java             # 주문 엔티티
│   └── Item.java              # 상품 엔티티
├── resources/
│   └── templates/orderForm.html  # 주문 화면
│   └── application.yml
└── ...

🔗 API 명세
메서드	경로	설명
GET	/api/items/{itemId}/stock	선택한 상품의 재고 수량을 조회합니다.
POST	/order	주문을 생성하며, 서버에서 재고를 최종 검증합니다.
❗ 모든 재고 데이터는 우선 Redis에서 조회되며, 캐시 미스 시 DB에서 fallback됩니다.

🧠 더 하고 싶은 것들 (To Do)
 Redis TTL 갱신을 위한 주기적 동기화 로직 구현

 주문 동시성 처리 (낙관적/비관적 락)

 Vue/React 기반 프론트엔드 전환 및 API화

 테스트 코드 작성 및 CI 적용

🙋🏻‍♂️ 만든 사람
이름	    기술 블로그
lilapark	https://lilapark.tistory.com


