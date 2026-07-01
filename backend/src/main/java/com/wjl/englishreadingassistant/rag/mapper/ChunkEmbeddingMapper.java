package com.wjl.englishreadingassistant.rag.mapper;

import com.wjl.englishreadingassistant.rag.entity.ChunkEmbedding;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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


    @Select("""
       select *
       from chunk_embedding
       order by embedding <=> cast(#{embedding} as vector)
       limit #{topK}
       """)
    List<ChunkEmbedding> searchSimilar(
            @Param("embedding") String embedding,
            @Param("topK") Integer limit
    );

    @Select("""
    select count(*)
    from chunk_embedding
    """)
    Integer count();


}
