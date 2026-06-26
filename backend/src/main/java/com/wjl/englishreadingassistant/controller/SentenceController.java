package com.wjl.englishreadingassistant.controller;

import com.wjl.englishreadingassistant.dto.SentenceRequest;
import com.wjl.englishreadingassistant.entity.SentenceAnalysis;
import com.wjl.englishreadingassistant.service.AIService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/sentence")
public class SentenceController {

    private final AIService aiService;

    public SentenceController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/analyze")
    public SentenceAnalysis analyze(@RequestBody SentenceRequest req) {

        return aiService.analyzeAndSave(
                req.getUserId(),
                req.getBookId(),
                req.getChapterId(),
                req.getSentence()
        );
    }
}
