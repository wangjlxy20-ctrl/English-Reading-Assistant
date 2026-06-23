package com.wjl.englishreadingassistant.mapper;
import com.wjl.englishreadingassistant.entity.Word;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface WordMapper {
    @Insert("""
        insert into word
        (user_id,chapter_id,word,meaning,example)
        values
        (#{userId},#{chapterId},#{word},#{meaning},#{example})
""")void insert(Word word);

    @Select("""
   select * 
   from word
   where user_id=#{userId}
   order by create_time desc
   """)
    List<Word> findByUserId(Long userId);

    @Delete("""
    delete from word 
    where id=#{id}
    """)void deleteById(Long id);
}
