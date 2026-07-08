package com.wjl.englishreadingassistant.service.impl;

import com.wjl.englishreadingassistant.entity.ReadingRecord;
import com.wjl.englishreadingassistant.mapper.ReadingRecordMapper;
import com.wjl.englishreadingassistant.service.ReadingRecordService;
import com.wjl.englishreadingassistant.redis.service.RedisService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;


@Service
public class ReadingRecordServiceImpl implements ReadingRecordService {
    private final ReadingRecordMapper mapper;
    private final RedisService redisService;

    public ReadingRecordServiceImpl(ReadingRecordMapper mapper, RedisService redisService) {
        this.mapper = mapper;
        this.redisService = redisService;
    }


    @Override
    public List<ReadingRecord> findByUserId(Long userId) {
        return mapper.findByUserId(userId);
    }

    @Override
    public void save(ReadingRecord record){


        ReadingRecord oldRecord =
                mapper.findByUserAndBook(
                        record.getUserId(),
                        record.getBookId()
                );


        if(oldRecord == null){

            //The first reading
            mapper.insert(record);


        }else{

            //Previously read,refresh progress status
            record.setId(oldRecord.getId());

            mapper.update(record);

        }

        redisService.delete(
                buildCacheKey(record.getUserId(), record.getBookId())
        );

    }

    @Override
    public ReadingRecord findByUserAndBook(Long userId, Long bookId) {
        String key = buildCacheKey(userId,bookId);

        //Query cache
        ReadingRecord cache =
                redisService.get(key,ReadingRecord.class);

        if(cache != null){
            System.out.println("======Cache hit in redis======");
            return cache;
        }

        //Query database
        ReadingRecord record =
                mapper.findByUserAndBook(userId,bookId);
        //No matching data found in database
        if(record == null){
            return null;
        }

        //Write data to Redis with 30-minute expiration
        redisService.set(
                key,
                record,
                Duration.ofMinutes(30)
        );

        //Return result
        System.out.println("======Write in Redis======");

        return record;

        //return mapper.findByUserAndBook(userId,bookId);
    }

    private String buildCacheKey(Long useId,Long bookId){
        return "reading:" + useId + ":" + bookId;
    }



}
