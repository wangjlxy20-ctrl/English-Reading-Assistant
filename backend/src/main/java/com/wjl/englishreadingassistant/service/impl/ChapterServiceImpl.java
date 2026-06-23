package com.wjl.englishreadingassistant.service.impl;

import com.wjl.englishreadingassistant.entity.Chapter;
import com.wjl.englishreadingassistant.mapper.ChapterMapper;
import com.wjl.englishreadingassistant.service.ChapterService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ChapterServiceImpl implements ChapterService {
    private final ChapterMapper chapterMapper;

    public ChapterServiceImpl(ChapterMapper chapterMapper) {
        this.chapterMapper = chapterMapper;
    }


    @Override
    public List<Chapter> findByBookId(Long bookId) {
        return chapterMapper.findByBookId(bookId);
    }

    @Override
    public Chapter findById(Long id) {
        return chapterMapper.findById(id);
    }
}
