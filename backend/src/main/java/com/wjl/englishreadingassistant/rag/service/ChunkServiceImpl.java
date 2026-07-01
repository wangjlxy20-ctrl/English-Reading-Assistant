package com.wjl.englishreadingassistant.rag.service;


import com.wjl.englishreadingassistant.rag.dto.ChunkResult;
import com.wjl.englishreadingassistant.entity.Chapter;
import com.wjl.englishreadingassistant.entity.Chunk;
import com.wjl.englishreadingassistant.enums.EmbeddingStatusEnum;
import com.wjl.englishreadingassistant.rag.mapper.ChunkMapper;
import com.wjl.englishreadingassistant.rag.splitter.ChunkSplitter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChunkServiceImpl implements ChunkService {
    private final ChunkSplitter chunkSplitter;
    private final ChunkMapper chunkMapper;
    private final EmbeddingService embeddingService;
    public ChunkServiceImpl(ChunkSplitter chunkSplitter,
                            ChunkMapper chunkMapper, EmbeddingService embeddingService) {
        this.chunkSplitter = chunkSplitter;
        this.chunkMapper = chunkMapper;
        this.embeddingService = embeddingService;
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
            chunk.setEmbeddingStatus(EmbeddingStatusEnum.PENDING.getCode());
            chunkMapper.insert(chunk);
            System.out.println(chunk.getId());


            try {
                embeddingService.saveEmbedding(chunk.getId(),
                        chunk.getContent());

                chunkMapper.updateEmbeddingStatus(
                        chunk.getId(),
                        EmbeddingStatusEnum.SUCCESS.getCode()
                );
            } catch (Exception e) {
                chunkMapper.updateEmbeddingStatus(
                        chunk.getId(),
                        EmbeddingStatusEnum.FAILED.getCode()
                );

                e.printStackTrace();
            }


        }

    }
}
