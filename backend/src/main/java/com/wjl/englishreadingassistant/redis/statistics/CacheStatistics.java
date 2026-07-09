package com.wjl.englishreadingassistant.redis.statistics;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class CacheStatistics {

    //Total request count
    private final AtomicLong totalRequest =
            new AtomicLong();

    //Cache hit count
    private final AtomicLong hit =
            new AtomicLong();

    //Cache miss count
    private final AtomicLong miss =
            new AtomicLong();

    public void recordHit(){

        totalRequest.incrementAndGet();

        hit.incrementAndGet();

    }

    public void recordMiss(){

        totalRequest.incrementAndGet();

        miss.incrementAndGet();

    }

    public long getTotalRequest(){

        return totalRequest.get();

    }

    public long getHit(){

        return hit.get();

    }

    public long getMiss(){

        return miss.get();

    }

    public double getHitRate(){

        if(totalRequest.get()==0){

            return 0;

        }

        return hit.get()*100.0/totalRequest.get();

    }

    public void printStatistics() {

        System.out.println();
        System.out.println("========== Cache Statistics ==========");
        System.out.println("Total Request : " + getTotalRequest());
        System.out.println("Cache Hit     : " + getHit());
        System.out.println("Cache Miss    : " + getMiss());
        System.out.printf("Hit Rate      : %.2f%%%n", getHitRate());
        System.out.println("======================================");
        System.out.println();
    }

}
