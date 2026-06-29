package com.wjl.englishreadingassistant.rag;

import com.wjl.englishreadingassistant.dto.ChunkResult;

import java.util.List;

public interface ChunkSplitter {
    /*
    * Split one chapter content into
    * multiple chunks
    *
    * @Param content Full text of a single chapter
    * @return List of chunk texts
    * */

    List<ChunkResult> split(String content);

}
