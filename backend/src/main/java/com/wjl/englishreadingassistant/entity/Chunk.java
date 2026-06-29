package com.wjl.englishreadingassistant.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Chunk {
    private Long id;

    private Long bookId;

    private Long chapterId;

    private Integer chunkIndex;

    private String content;

    private Integer tokenCount;

    /*
    * 0 Embedding has not been generated yet
    * 1 Embedding has been generated
    * 2 Failed to generate Embedding
    * */

    private Integer embeddingStatus;

    private LocalDateTime createTime;
}
