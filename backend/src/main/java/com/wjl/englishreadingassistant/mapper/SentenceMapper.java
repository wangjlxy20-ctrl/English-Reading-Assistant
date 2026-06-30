package com.wjl.englishreadingassistant.mapper;


import com.wjl.englishreadingassistant.dto.SentenceRequest;
import com.wjl.englishreadingassistant.entity.SentenceAnalysis;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SentenceMapper {

    @Insert("""
        INSERT INTO sentence_analysis
        (
         book_id,
         chapter_id,
         sentence,
         meaning,
         grammar,
         key_phrases,
         difficulty)
        VALUES
        (
         #{bookId},
         #{chapterId},
         #{sa.sentence},
         #{sa.meaning},
         #{grammar} :: jsonb,
         #{keyPhrases} :: jsonb,
         #{sa.difficulty})
        """)void insert(
            @Param("sa")SentenceAnalysis sa,
            @Param("bookId") Long bookId,
            @Param("chapterId") Long chapterId,
            @Param("grammar") String grammar,
            @Param("keyPhrases") String keyPhrases
            );

}
