package com.wjl.englishreadingassistant.dto;


import lombok.Data;

import java.util.List;

@Data
public class SentenceAIResult {

    private String meaning;
    private List<String> grammar;
    private List<String> keyPhrases;
    private Integer difficulty;
}
