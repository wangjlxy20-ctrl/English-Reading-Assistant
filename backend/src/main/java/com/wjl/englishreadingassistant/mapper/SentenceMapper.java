package com.wjl.englishreadingassistant.mapper;


import com.wjl.englishreadingassistant.entity.SentenceAnalysis;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SentenceMapper {
    void insert(
            @Param("sa")SentenceAnalysis sa,
            @Param("userId") Long userId,
            @Param("bookId") Long bookId,
            @Param("chapterId") Long chapterId
            );

}
