package com.wjl.englishreadingassistant.mapper;

import com.wjl.englishreadingassistant.entity.Book;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BookMapper {
    @Select("select * from book order by id desc ")
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

    @Update("""
    update book
    set total_chapters = #{totalChapters}
    where id = #{id}
    """)
    void updateTotalChapters(
      @Param("id") Long id,
      @Param("totalChapters") Integer totalChapters
    );




}
