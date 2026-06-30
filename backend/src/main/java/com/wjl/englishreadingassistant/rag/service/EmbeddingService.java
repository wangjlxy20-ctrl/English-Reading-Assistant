package com.wjl.englishreadingassistant.rag.service;

public interface EmbeddingService {
    void saveEmbedding(Long sentenceId,String content);
}
