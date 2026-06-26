package com.wjl.englishreadingassistant.entity;


import lombok.Data;

import java.util.List;

@Data
public class SentenceAnalysis {
    private Long id;
    private Long bookId;
    private Long chapterId;

    private String sentence;
    private String meaning;

    private List<String> grammar;
    private List<String> keyPhrases;

    private Integer difficulty;

}
