package com.wjl.englishreadingassistant.controller;

import com.wjl.englishreadingassistant.entity.ReadingRecord;
import com.wjl.englishreadingassistant.rag.embedding.EmbeddingClient;
import com.wjl.englishreadingassistant.service.ReadingRecordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    private final EmbeddingClient embeddingClient;

    private final ReadingRecordService readingRecordService;

    public TestController(EmbeddingClient embeddingClient, ReadingRecordService readingRecordService) {
        this.embeddingClient = embeddingClient;
        this.readingRecordService = readingRecordService;
    }

    @GetMapping("/test/embedding")
    public String testEmbedding() {

        embeddingClient.embed("Hello World");

        return "success";
    }

    @GetMapping("/test")
    public ReadingRecord test(){
        return readingRecordService.findByUserAndBook(1L,1L);
    }
}