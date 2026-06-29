package com.wjl.englishreadingassistant.mapper;


import com.wjl.englishreadingassistant.entity.Chunk;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
}
