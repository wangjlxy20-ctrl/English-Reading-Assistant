package com.wjl.englishreadingassistant.controller;


import com.wjl.englishreadingassistant.dto.WordRequest;
import com.wjl.englishreadingassistant.service.AIService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/ai")
public class AIController {

    private final AIService aiService;

    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/ask")
    public String askQwen(String question){
        return aiService.askQwen(question);
    }

    @PostMapping("/word")
    public String analyzeWord(
            @RequestBody WordRequest request
            ){

        System.out.println("Receive AI request: ");
        return aiService.analyzeWord(
                request.getWord()
        );
    }


}
