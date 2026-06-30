package com.wjl.englishreadingassistant.rag.mapper;

import com.wjl.englishreadingassistant.rag.entity.SentenceEmbedding;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SentenceEmbeddingMapper {

    @Insert("""
    insert into sentence_embedding
        (
         sentence_id,
         content,
         embedding
        )
    values (
            #{sentenceId},
            #{content},
            CAST(#{embedding} AS vector)
    )
    """)
    void insert(SentenceEmbedding embedding);


}
