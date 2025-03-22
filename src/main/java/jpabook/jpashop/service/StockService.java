package jpabook.jpashop.service;

import jakarta.annotation.PostConstruct;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class StockService {

    private final ItemService itemService;
    private final StringRedisTemplate redisTemplate;

    // Redis 키 접두사 및 TTL 설정
    private static final String STOCK_KEY_PREFIX = "stock:";
    private static final long CACHE_TTL_HOURS = 24;

    /**
     * 애플리케이션 시작 시 모든 상품의 재고 정보를 Redis에 초기화
     */
    @PostConstruct
    public void initializeStockCache() {
        log.info("상품 재고 정보를 Redis에 초기화합니다.");
        List<Item> items = itemService.findItems();

        for (Item item : items) {
            updateStockInRedis(item.getId(), item.getStockQuantity());
            log.info("상품 ID: {}, 재고: {} - Redis에 캐싱됨", item.getId(), item.getStockQuantity());
        }
    }

    /**
     * 특정 상품의 재고 정보를 조회
     */
    public int getStockQuantity(Long itemId) {
        // Redis에서 재고 정보 조회
        String stockKey = STOCK_KEY_PREFIX + itemId;
        String stockValue = redisTemplate.opsForValue().get(stockKey);

        if (stockValue != null) {
            log.info("Redis 캐시에서 재고 정보 조회: {}", stockValue);
            return Integer.parseInt(stockValue);
        } else {
            log.info("Redis 캐시에 정보가 없어 DB에서 조회합니다.");
            // Redis에 없으면 DB에서 조회하고 다시 캐싱
            Item item = itemService.findOne(itemId);

            if (item != null) {
                int stockQuantity = item.getStockQuantity();
                updateStockInRedis(itemId, stockQuantity);
                log.info("DB에서 조회한 재고 정보를 Redis에 캐싱: {}", stockQuantity);
                return stockQuantity;
            } else {
                log.warn("상품 ID: {}를 찾을 수 없습니다.", itemId);
                return 0; // 상품이 없는 경우 재고 0 반환
            }
        }
    }

    /**
     * 주문 처리 후 재고 업데이트
     */
    @Transactional
    public void updateStockAfterOrder(Long itemId, int newStockQuantity) {
        updateStockInRedis(itemId, newStockQuantity);
        log.info("주문 처리 후 Redis 재고 업데이트 - 상품 ID: {}, 남은 재고: {}", itemId, newStockQuantity);
    }

    /**
     * 주문 취소 후 재고 업데이트
     */
    @Transactional
    public void updateStockAfterCancel(Long itemId, int newStockQuantity) {
        updateStockInRedis(itemId, newStockQuantity);
        log.info("주문 취소 후 Redis 재고 업데이트 - 상품 ID: {}, 업데이트된 재고: {}", itemId, newStockQuantity);
    }

    /**
     * Redis에 재고 정보 업데이트
     */
    private void updateStockInRedis(Long itemId, int stockQuantity) {
        String stockKey = STOCK_KEY_PREFIX + itemId;
        redisTemplate.opsForValue().set(
                stockKey,
                String.valueOf(stockQuantity),
                CACHE_TTL_HOURS,
                TimeUnit.HOURS
        );
    }
}