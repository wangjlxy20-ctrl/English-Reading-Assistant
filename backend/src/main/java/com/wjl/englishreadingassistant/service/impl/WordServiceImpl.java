package com.wjl.englishreadingassistant.service.impl;

import com.wjl.englishreadingassistant.entity.Word;
import com.wjl.englishreadingassistant.mapper.WordMapper;
import com.wjl.englishreadingassistant.service.WordService;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class WordServiceImpl implements WordService {

    public final WordMapper wordMapper;

    public WordServiceImpl(WordMapper wordMapper) {
        this.wordMapper = wordMapper;
    }

    @Override
    public void save(Word word) {
        Word exist =
                wordMapper.findByUserIdAndWord(
                        word.getUserId(),
                        word.getWord()
                );

        if(exist == null){
            wordMapper.insert(word);
        }

    }

    @Override
    public List<Word> findByUserId(Long userId) {
        return wordMapper.findByUserId(userId);
    }

    @Override
    public void delete(Long id) {
        wordMapper.deleteById(id);
    }

    @Override
    public void updateWordInfo(Word word) {
        wordMapper.updateWordInfo(word);
    }
}
