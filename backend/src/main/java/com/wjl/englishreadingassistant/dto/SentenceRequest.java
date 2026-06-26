package com.wjl.englishreadingassistant.dto;

import lombok.Data;

@Data
public class SentenceRequest {

    private Long userId;
    private Long bookId;
    private Long chapterId;
    private String sentence;
}
