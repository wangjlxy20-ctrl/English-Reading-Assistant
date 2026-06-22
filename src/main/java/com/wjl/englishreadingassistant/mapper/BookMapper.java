package com.wjl.englishreadingassistant.mapper;

import com.wjl.englishreadingassistant.entity.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BookMapper {
    @Select("select * from book")
    List<Book> findAll();
}
