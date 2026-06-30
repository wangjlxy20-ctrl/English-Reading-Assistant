package com.wjl.englishreadingassistant.rag.service.impl;

import com.wjl.englishreadingassistant.rag.embedding.EmbeddingClient;
import com.wjl.englishreadingassistant.rag.entity.SentenceEmbedding;
import com.wjl.englishreadingassistant.rag.mapper.SentenceEmbeddingMapper;
import com.wjl.englishreadingassistant.rag.service.EmbeddingService;

import java.util.List;

public class EmbeddingServiceImpl implements EmbeddingService {

    private final EmbeddingClient embeddingClient;

    private final SentenceEmbeddingMapper sentenceEmbeddingMapper;

    public EmbeddingServiceImpl(EmbeddingClient embeddingClient, SentenceEmbeddingMapper sentenceEmbeddingMapper) {
        this.embeddingClient = embeddingClient;
        this.sentenceEmbeddingMapper = sentenceEmbeddingMapper;
    }

    @Override
    public void saveEmbedding(Long sentenceId, String content) {

        //Invoke DashScope
        List<Float> vector = embeddingClient.embed(content);

        //List<Float> -> String
        String embedding = vector.toString();

        //build entity object
        SentenceEmbedding entity = new SentenceEmbedding();
        entity.setSentenceId(sentenceId);
        entity.setContent(content);
        entity.setEmbedding(embedding);

        //save
        sentenceEmbeddingMapper.insert(entity);
    }
}
