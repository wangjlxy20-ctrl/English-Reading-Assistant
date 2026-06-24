package com.wjl.englishreadingassistant.controller;

import com.wjl.englishreadingassistant.entity.Word;
import com.wjl.englishreadingassistant.mapper.WordMapper;
import com.wjl.englishreadingassistant.service.WordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/words")
@CrossOrigin
public class WordController {

    private final WordService wordService;
    private final WordMapper wordMapper;

    public WordController(WordService wordService, WordMapper wordMapper) {
        this.wordService = wordService;
        this.wordMapper = wordMapper;
    }

    @PostMapping
    public String save(
            @RequestBody Word word){
        Word existWord =
                wordMapper.findByUserIdAndWord(
                        word.getUserId(),
                        word.getWord()
                );
        if(existWord != null){
            return "already exists";
        }
        wordService.save(word);
        return "saved";
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

