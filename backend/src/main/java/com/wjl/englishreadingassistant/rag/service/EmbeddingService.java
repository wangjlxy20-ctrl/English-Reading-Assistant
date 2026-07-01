package com.wjl.englishreadingassistant.rag.service;

import com.wjl.englishreadingassistant.rag.entity.ChunkEmbedding;

import java.util.List;

public interface EmbeddingService {
    void saveEmbedding(Long chunkId, String content);

    List<ChunkEmbedding> searchSimilar(String question,Integer topK);
}
