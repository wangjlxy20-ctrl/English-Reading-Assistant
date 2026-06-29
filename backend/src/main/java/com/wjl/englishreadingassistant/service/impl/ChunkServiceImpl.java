package com.wjl.englishreadingassistant.service.impl;


import com.wjl.englishreadingassistant.dto.ChunkResult;
import com.wjl.englishreadingassistant.entity.Chapter;
import com.wjl.englishreadingassistant.entity.Chunk;
import com.wjl.englishreadingassistant.mapper.ChunkMapper;
import com.wjl.englishreadingassistant.rag.ChunkSplitter;
import com.wjl.englishreadingassistant.service.ChunkService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChunkServiceImpl implements ChunkService {
    private final ChunkSplitter chunkSplitter;
    private final ChunkMapper chunkMapper;

    public ChunkServiceImpl(ChunkSplitter chunkSplitter,
                            ChunkMapper chunkMapper) {
        this.chunkSplitter = chunkSplitter;
        this.chunkMapper = chunkMapper;
    }

    @Override
    @Transactional
    public void generateChunks(Chapter chapter) {
        //Prevent repeated import operation
        chunkMapper.deleteByChapterId(chapter.getId());

        //Call the text segmentation tool
        List<ChunkResult> results =
                chunkSplitter.split(chapter.getContent());

        int index = 0;

        for (ChunkResult result : results) {
            Chunk chunk = new Chunk();
            chunk.setBookId(chapter.getBookId());
            chunk.setChapterId(chapter.getId());
            chunk.setChunkIndex(index++);
            chunk.setContent(result.getContent());
            chunk.setTokenCount(result.getTokenCount());

            //Status code 0 : Waiting for embedding vector generation
            chunk.setEmbeddingStatus(0);
            chunkMapper.insert(chunk);
        }

    }
}
