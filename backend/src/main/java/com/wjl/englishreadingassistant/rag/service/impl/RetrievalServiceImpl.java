package com.wjl.englishreadingassistant.rag.service.impl;

import com.wjl.englishreadingassistant.entity.Chunk;
import com.wjl.englishreadingassistant.rag.entity.ChunkEmbedding;
import com.wjl.englishreadingassistant.rag.mapper.ChunkMapper;
import com.wjl.englishreadingassistant.rag.service.EmbeddingService;
import com.wjl.englishreadingassistant.rag.service.RetrievalService;
import com.wjl.englishreadingassistant.redis.service.RedisService;
import com.wjl.englishreadingassistant.redis.util.RedisKey;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RetrievalServiceImpl implements RetrievalService {

    private final EmbeddingService embeddingService;
    private final ChunkMapper chunkMapper;
    private final RedisService redisService;


    public RetrievalServiceImpl(
            EmbeddingService embeddingService,
            ChunkMapper chunkMapper, RedisService redisService) {
        this.embeddingService = embeddingService;
        this.chunkMapper = chunkMapper;
        this.redisService = redisService;
    }

    @Override
    public List<Chunk> retrieve(String question) {
        //Add methods to query data from Redis cache
        String key = RedisKey.rag(question);

        List<Chunk> cache =
                redisService.get(
                        key,
                        List.class
                );

        if (cache != null) {
            System.out.println("[Redis] RAG Cache hit : " + key);
            return cache;
        }

        // 1. 相似向量检索
        List<ChunkEmbedding> embeddings =
                embeddingService.searchSimilar(question, 3);

        if (embeddings.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. 根据 chunkId 查询真正的 chunk
        List<Chunk> result = new ArrayList<>();

        for (ChunkEmbedding embedding : embeddings) {

            Chunk chunk =
                    chunkMapper.findById(embedding.getChunkId());

            if (chunk != null) {
                result.add(chunk);
            }
        }

        return result;
    }
}
