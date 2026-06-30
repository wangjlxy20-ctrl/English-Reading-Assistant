package com.wjl.englishreadingassistant.rag.embedding;

import java.util.List;

public interface EmbeddingClient {

    /*
    * Get embedding vector of text
    * */
    List<Float> embed(String text);
}
