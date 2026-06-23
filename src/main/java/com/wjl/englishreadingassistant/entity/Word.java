package com.wjl.englishreadingassistant.entity;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Word {
    private Long id;
    private Long userId;
    private Long chapterId;
    private String word;
    private String meaning;
    private String example;
    private LocalDateTime createTime;
}
