package com.wjl.englishreadingassistant.controller;

import com.wjl.englishreadingassistant.entity.Word;
import com.wjl.englishreadingassistant.service.WordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/words")
public class WordController {

    private final WordService wordService;

    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @PostMapping
    public String save(
            @RequestBody Word word){
        wordService.save(word);
        return "success";
    }

    @GetMapping("/{userId}")
    public List<Word> list(
            @PathVariable Long userId
    ){
        return wordService.findByUserId(userId);
    }

    @DeleteMapping("/{id}")
    public String delete(
            @PathVariable Long id
    ){
        wordService.delete(id);
        return "success";
    }
}

