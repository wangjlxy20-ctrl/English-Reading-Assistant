package com.wjl.englishreadingassistant.rag.entity;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SentenceEmbedding {
    private Long id;
    private Long sentenceId;
    private String content;
    private String embedding;
    private LocalDateTime createdAt;
}
