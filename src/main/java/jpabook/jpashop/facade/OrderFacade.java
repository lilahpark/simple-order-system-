package jpabook.jpashop.facade;

import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final RedissonClient redissonClient;
    private final OrderService orderService;

    public Long order(Long memberId, Long itemId, int count) {
        RLock lock = redissonClient.getLock("lock:stock:" + itemId);
        try {
            boolean isLocked = lock.tryLock(10, 1, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new IllegalStateException("락 획득 실패 - 재시도 하세요");
            }

            //트랜잭션은 이 안에서 시작
            return orderService.order(memberId, itemId, count);

        } catch (InterruptedException e) {
            throw new RuntimeException("락 처리 중 인터럽트 발생", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
