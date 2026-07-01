package com.wjl.englishreadingassistant.rag.dto;

import com.wjl.englishreadingassistant.entity.Chunk;
import lombok.Data;

import java.util.List;

@Data
public class RagAnswer {
    private String answer;
    private List<Chunk> sources;

    public RagAnswer(String answer, List<Chunk> sources) {
        this.answer = answer;
        this.sources = sources;
    }
}
