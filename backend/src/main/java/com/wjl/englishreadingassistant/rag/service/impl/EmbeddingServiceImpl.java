package com.wjl.englishreadingassistant.rag.service.impl;

import com.wjl.englishreadingassistant.rag.embedding.EmbeddingClient;
import com.wjl.englishreadingassistant.rag.entity.ChunkEmbedding;
import com.wjl.englishreadingassistant.rag.mapper.ChunkEmbeddingMapper;
import com.wjl.englishreadingassistant.rag.service.EmbeddingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmbeddingServiceImpl implements EmbeddingService {

    private final EmbeddingClient embeddingClient;

    private final ChunkEmbeddingMapper chunkEmbeddingMapper;

    public EmbeddingServiceImpl(EmbeddingClient embeddingClient, ChunkEmbeddingMapper chunkEmbeddingMapper) {
        this.embeddingClient = embeddingClient;
        this.chunkEmbeddingMapper = chunkEmbeddingMapper;
    }

    @Override
    public void saveEmbedding(Long chunkId, String content) {

        //Invoke DashScope
        List<Float> vector = embeddingClient.embed(content);

        //List<Float> -> String
        String embedding = vector.toString();

        //build entity object
        ChunkEmbedding entity = new ChunkEmbedding();
        entity.setChunkId(chunkId);
        entity.setContent(content);
        entity.setEmbedding(embedding);

        //save
        chunkEmbeddingMapper.insert(entity);
    }
}
