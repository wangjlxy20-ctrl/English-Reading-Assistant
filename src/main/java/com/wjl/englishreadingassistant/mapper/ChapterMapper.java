package com.wjl.englishreadingassistant.mapper;

import com.wjl.englishreadingassistant.entity.Chapter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChapterMapper {

    @Select(
            "select * from chapter where book_id = #{bookId}"
    )
    List<Chapter> findByBookId(Long bookId);
}
