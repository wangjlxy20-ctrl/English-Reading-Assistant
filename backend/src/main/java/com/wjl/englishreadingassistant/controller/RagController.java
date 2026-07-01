package com.wjl.englishreadingassistant.controller;

import com.wjl.englishreadingassistant.entity.Chunk;
import com.wjl.englishreadingassistant.rag.dto.RagAnswer;
import com.wjl.englishreadingassistant.rag.mapper.ChunkEmbeddingMapper;
import com.wjl.englishreadingassistant.rag.service.RagAnswerService;
import com.wjl.englishreadingassistant.rag.service.RetrievalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/rag")
public class RagController {

    private final RetrievalService retrievalService;

    private final RagAnswerService ragAnswerService;

    private final ChunkEmbeddingMapper chunkEmbeddingMapper;
    public RagController(RetrievalService retrievalService, RagAnswerService ragAnswerService, ChunkEmbeddingMapper chunkEmbeddingMapper) {
        this.retrievalService = retrievalService;
        this.ragAnswerService = ragAnswerService;
        this.chunkEmbeddingMapper = chunkEmbeddingMapper;
    }

    @GetMapping("/search")
    public List<Chunk> search(
            @RequestParam String question) {

        return retrievalService.retrieve(question);
    }

    @GetMapping("/ask")
    public RagAnswer ask(@RequestParam String question) {
        return ragAnswerService.answer(question);
    }

    @GetMapping("/count")
    public Integer count() {
        return chunkEmbeddingMapper.count();
    }

}
