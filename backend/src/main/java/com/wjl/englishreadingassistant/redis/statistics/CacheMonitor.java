package com.wjl.englishreadingassistant.redis.statistics;

import org.springframework.stereotype.Component;

@Component
public class CacheMonitor {

    public void print(
            String key,
            boolean hit,
            long cost,
            CacheStatistics statistics) {

        System.out.println();
        System.out.println("========== Cache Statistics ==========");
        System.out.println("Cache Key     : " + key);
        System.out.println("Cache Status  : " + (hit ? "HIT" : "MISS"));
        System.out.println("Redis Cost    : " + cost + " ms");
        System.out.println();
        System.out.println("Total Request : " + statistics.getTotalRequest());
        System.out.println("Cache Hit     : " + statistics.getHit());
        System.out.println("Cache Miss    : " + statistics.getMiss());
        System.out.printf("Hit Rate      : %.2f%%%n", statistics.getHitRate());
        System.out.println("======================================");
        System.out.println();
    }
}
