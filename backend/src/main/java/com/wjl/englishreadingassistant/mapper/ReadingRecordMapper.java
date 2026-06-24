package com.wjl.englishreadingassistant.mapper;

import com.wjl.englishreadingassistant.entity.ReadingRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReadingRecordMapper {
    //retrieve all reading records of the user
    @Select(
            "select * from reading_record where user_id = #{userId}"
    )
    List<ReadingRecord> findByUserId(Long userId);


    //Check whether reading records exist for a certain chapter
    @Select("""
        select *
        from reading_record
        where user_id=#{userId}
        and book_id=#{bookId}
        and chapter_id=#{chapterId}
        """)
    ReadingRecord findRecord(
            Long userId,
            Long bookId,
            Long chapterId
    );

    //Add
    @Insert(
            """
            insert into reading_record
            (user_id,book_id,chapter_id,progress,last_read_time)
            values
            ( #{userId},#{bookId},#{chapterId},#{progress},now() )
            """
    )void insert(ReadingRecord record);


    //Update
    @Update("""
        update reading_record
        set chapter_id = #{chapterId},
            progress = #{progress},
            last_read_time = now()
        where id = #{id}
    """)
    void update(ReadingRecord record);

    @Select("""
    select * from reading_record
    where user_id = #{userId} and book_id = #{bookId}
    """)
    ReadingRecord findByUserAndBook(
            @Param("userId") Long userId,
            @Param("bookId") Long bookId
    );

}
