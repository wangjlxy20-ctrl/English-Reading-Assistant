package com.wjl.englishreadingassistant.controller;

import com.wjl.englishreadingassistant.rag.embedding.EmbeddingClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    private final EmbeddingClient embeddingClient;

    public TestController(EmbeddingClient embeddingClient) {
        this.embeddingClient = embeddingClient;
    }

    @GetMapping("/test/embedding")
    public String testEmbedding() {

        embeddingClient.embed("Hello World");

        return "success";
    }
}