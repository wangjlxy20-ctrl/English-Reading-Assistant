package com.wjl.englishreadingassistant.rag.service;

import com.wjl.englishreadingassistant.entity.Chapter;

public interface ChunkService {
    /*
    * Generate chunks based on Chapter
    * @Param chapter. Object of chapter
    * */

    void generateChunks(Chapter chapter);

}
