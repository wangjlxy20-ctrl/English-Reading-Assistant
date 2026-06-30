package com.wjl.englishreadingassistant.rag.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChunkResult {
    /*
    * The content of Chunk
    * */
    private String content;

    /*
    * Word count of chunk
    * (can be upgraded to real token count later)
    * */
    private Integer tokenCount;
}
