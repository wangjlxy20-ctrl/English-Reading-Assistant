package com.wjl.englishreadingassistant.rag.entity;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChunkEmbedding {
    private Long id;
    private Long chunkId;
    private String content;
    private String embedding;
    private LocalDateTime createdAt;
}
