package jpabook.jpashop.controller;

import jakarta.annotation.PostConstruct;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StockController {

    private final ItemService itemService;
    private final StringRedisTemplate redisTemplate;
    private final StockService stockService;


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
            String stockKey = STOCK_KEY_PREFIX + item.getId();
            redisTemplate.opsForValue().set(
                    stockKey,
                    String.valueOf(item.getStockQuantity()),
                    CACHE_TTL_HOURS,
                    TimeUnit.HOURS
            );

            log.info("상품 ID: {}, 재고: {} - Redis에 캐싱됨", item.getId(), item.getStockQuantity());
        }
    }

    /**
     * 특정 상품의 재고 정보를 조회하는 API
     */
    @GetMapping("/api/items/{itemId}/stock")
    public ResponseEntity<Map<String, Integer>> getItemStock(@PathVariable Long itemId) {
        log.info("상품 ID: {}의 재고 정보 조회 요청", itemId);

        int stockQuantity = stockService.getStockQuantity(itemId);

        if (stockQuantity > 0) {
            Map<String, Integer> response = new HashMap<>();
            response.put("stock", stockQuantity);
            return ResponseEntity.ok(response);
        } else {
            log.warn("상품 ID: {}의 재고가 없거나 상품을 찾을 수 없습니다.", itemId);
            return ResponseEntity.notFound().build();
        }

    }
}
