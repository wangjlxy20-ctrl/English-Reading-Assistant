package com.wjl.englishreadingassistant.rag.service;

public interface EmbeddingService {
    void saveEmbedding(Long chunkId, String content);
}
