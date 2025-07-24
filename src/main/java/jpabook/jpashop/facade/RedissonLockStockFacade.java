package jpabook.jpashop.facade;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedissonLockStockFacade {

    private final RedissonClient redissonClient;
    private final ItemService itemService;

    @Transactional
    public void decrease(Long itemId, int count) {
        RLock lock = redissonClient.getLock("lock:stock:" + itemId);

        try {
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);
            if (!available) {
                throw new IllegalStateException("락 획득 실패");
            }

            Item item = itemService.findOne(itemId);
            item.removeStock(count); // 재고 감소 로직 (JPA 엔티티 메서드)

        } catch (InterruptedException e) {
            throw new RuntimeException("락 획득 중단", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

}
