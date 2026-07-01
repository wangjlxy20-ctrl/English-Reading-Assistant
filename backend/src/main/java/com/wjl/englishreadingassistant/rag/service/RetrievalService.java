package com.wjl.englishreadingassistant.rag.service;

import com.wjl.englishreadingassistant.entity.Chunk;

import java.util.List;

public interface RetrievalService {
    List<Chunk> retrieve(String question);
}
