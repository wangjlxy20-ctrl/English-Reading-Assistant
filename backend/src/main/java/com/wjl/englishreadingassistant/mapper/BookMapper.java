package com.wjl.englishreadingassistant.mapper;

import com.wjl.englishreadingassistant.entity.Book;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BookMapper {
    @Select("select * from book")
    List<Book> findAll();

    @Insert("""
    insert into book 
    (title,total_chapters)
    values 
    (
    #{title},#{totalChapters}        
    )   
    """)
    @Options(useGeneratedKeys = true,keyProperty = "id")
    void insert(Book book);
}
