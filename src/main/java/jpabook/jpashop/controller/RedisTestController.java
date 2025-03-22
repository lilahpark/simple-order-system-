package jpabook.jpashop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.redis.core.StringRedisTemplate;

@RestController
@RequiredArgsConstructor
public class RedisTestController {
    private final StringRedisTemplate redisTemplate;

    @GetMapping("/redis-test")
    public String testRedis() {
        // 데이터 저장
        redisTemplate.opsForValue().set("test:key", "Hello Redis!");

        // 데이터 조회
        String value = redisTemplate.opsForValue().get("test:key");

        return "Redis Test Result: " + value;
    }
}
