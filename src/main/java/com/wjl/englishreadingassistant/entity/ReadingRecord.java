package com.wjl.englishreadingassistant.entity;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReadingRecord {
    private Long id;
    private Long userId;
    private Long bookId;
    private Long chapterId;
    private Integer progress;
    private LocalDateTime lastReadTime;
}
