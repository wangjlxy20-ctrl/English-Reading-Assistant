package com.wjl.englishreadingassistant.mapper;

import com.wjl.englishreadingassistant.entity.Chapter;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChapterMapper {

    @Select("""
    select * 
    from chapter 
    where book_id = #{bookId}
    order by chapter_no
    """
    )
    List<Chapter> findByBookId(Long bookId);

    @Select("""
    select *
    from chapter
    where id=#{id}
    """)
    Chapter findById(Long id);

    @Insert("""
        insert into chapter
        (book_id,chapter_no,title,content)
        values 
        (
         #{bookId},
         #{chapterNo},
         #{title},
         #{content}
         
         )
    """)
    @Options(useGeneratedKeys = true,keyProperty = "id")
    void insert(Chapter chapter);
}
