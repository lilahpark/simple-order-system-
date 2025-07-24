package jpabook.jpashop.facade;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class OrderConcurrencyTest {

    @Autowired
    EntityManager em;
    @Autowired
    MemberService memberService;
    @Autowired
    ItemService itemService;
    @Autowired OrderFacade orderFacade;
    @Autowired
    OrderRepository orderRepository;

    private Long memberId;
    private Long itemId;

    @BeforeEach
    public void setUp() {
        Member member = new Member();
        member.setName("동시성회원");
        member.setAddress(new Address("서울", "한강로", "101-101"));
        em.persist(member);
        memberId = member.getId();

        Book book = new Book();
        book.setName("Redisson 동시성 BOOK");
        book.setPrice(10000);
        book.setStockQuantity(99); // 정확히 99개로 세팅
        em.persist(book);
        itemId = book.getId();
    }

    @Test
    @Timeout(20)
    public void 동시에_99개의_주문요청_재고는_0이되어야_한다() throws InterruptedException {
        int threadCount = 99;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    orderFacade.order(memberId, itemId, 1);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                    System.out.println("실패: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // 영속성 컨텍스트 초기화 후 재조회
        em.flush();
        em.clear();
        Item item = itemService.findOne(itemId);

        System.out.println("=== 결과 ===");
        System.out.println("성공 주문 수: " + successCount.get());
        System.out.println("실패 주문 수: " + failCount.get());
        System.out.println("최종 재고: " + item.getStockQuantity());

        assertThat(successCount.get()).isEqualTo(99); // 99개 성공
        assertThat(failCount.get()).isEqualTo(0);     // 실패 없음
        assertThat(item.getStockQuantity()).isEqualTo(0); // 재고 0
    }
}