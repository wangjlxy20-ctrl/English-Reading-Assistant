package com.wjl.englishreadingassistant.service.impl;

import com.wjl.englishreadingassistant.entity.ReadingRecord;
import com.wjl.englishreadingassistant.mapper.ReadingRecordMapper;
import com.wjl.englishreadingassistant.service.ReadingRecordService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ReadingRecordServiceImpl implements ReadingRecordService {
    private final ReadingRecordMapper mapper;

    public ReadingRecordServiceImpl(ReadingRecordMapper mapper) {
        this.mapper = mapper;
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

    }



}
