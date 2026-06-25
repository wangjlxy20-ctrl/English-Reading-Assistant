package com.wjl.englishreadingassistant.mapper;

import com.wjl.englishreadingassistant.entity.Chapter;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChapterMapper {

    @Select(
            "select * from chapter where book_id = #{bookId}"
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
    void insert(Chapter chapter);
}
