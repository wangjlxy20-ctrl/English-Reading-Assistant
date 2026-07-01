package com.wjl.englishreadingassistant.rag.mapper;


import com.wjl.englishreadingassistant.entity.Chunk;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ChunkMapper {
    @Insert("""
    insert into chunk(
        book_id,
        chapter_id,
        chunk_index,
        content,
        token_count,
        embedding_status
    )
    values (
            #{bookId},
            #{chapterId},
            #{chunkIndex},
            #{content},
            #{tokenCount},
            #{embeddingStatus}
    )
    """)
    @Options(useGeneratedKeys = true,keyProperty = "id")
    void insert(Chunk chunk);



    @Select("""
    select * 
    from chunk
    where chapter_id = #{chapterId}
    order by chunk_index 
""")
    List<Chunk> findByChapterId(Long chapterId);


    @Delete("""
    delete from chunk
    where chapter_id = #{chapterId}
    """)
    void deleteByChapterId(Long chapterId);

    @Update("""
    update chunk
    set embedding_status = #{status}
    where id = #{id}
    """)
    void updateEmbeddingStatus(
            @Param("id") Long id,
            @Param("status") Integer status
    );


}
