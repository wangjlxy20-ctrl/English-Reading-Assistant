package com.wjl.englishreadingassistant.rag.mapper;

import com.wjl.englishreadingassistant.rag.entity.ChunkEmbedding;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChunkEmbeddingMapper {

    @Insert("""
    insert into chunk_embedding
        (
         chunk_id,
         content,
         embedding
        )
    values (
            #{chunkId},
            #{content},
            CAST(#{embedding} AS vector)
    )
    """)
    void insert(ChunkEmbedding embedding);


}
