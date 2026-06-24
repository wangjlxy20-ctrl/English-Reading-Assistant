package com.wjl.englishreadingassistant.service;

import com.wjl.englishreadingassistant.entity.Word;

import java.util.List;

public interface WordService {

    void save(Word word);


    List<Word> findByUserId(Long userId);


    void delete(Long id);

    void updateWordInfo(Word word);
}
