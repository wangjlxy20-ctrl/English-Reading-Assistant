package com.wjl.englishreadingassistant.controller;

import com.wjl.englishreadingassistant.entity.Chapter;
import com.wjl.englishreadingassistant.service.ChapterService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chapters")
@CrossOrigin
public class ChapterController {
    private final ChapterService chapterService;

    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @GetMapping("/book/{bookId}")
    public List<Chapter> list(
            @PathVariable Long bookId
    ){
        return chapterService.findByBookId(bookId);
    }

    @GetMapping("/{id}")
    public Chapter detail(
            @PathVariable Long id){

        return chapterService.findById(id);

    }
}
