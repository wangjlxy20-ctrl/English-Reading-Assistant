package com.wjl.englishreadingassistant.service;

import com.wjl.englishreadingassistant.entity.ReadingRecord;

import java.util.List;


public interface ReadingRecordService {
    List<ReadingRecord> findByUserId(Long userId);

    void save(ReadingRecord record);

    ReadingRecord findByUserAndBook(Long uerId,Long bookId);
}
