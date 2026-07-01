package com.wjl.englishreadingassistant.rag.service;

import com.wjl.englishreadingassistant.rag.dto.RagAnswer;

public interface RagAnswerService {
    RagAnswer answer(String question);
}
