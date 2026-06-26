package com.wjl.englishreadingassistant.service;
import com.wjl.englishreadingassistant.entity.Chapter;

import java.util.List;

public interface ChapterService {
    List<Chapter> findByBookId(Long bookId);
    Chapter findById(Long id);

    List<Chapter> parseChapters(String content);
}
