package com.wjl.englishreadingassistant.mapper;

import com.wjl.englishreadingassistant.entity.AIChat;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AIChatMapper {

    @Insert("""
        insert into ai_chat
        (
            user_id,
            question,
            answer,
            context
        )
        values
        (
            #{userId},
            #{question},
            #{answer},
            #{context}
        )
        """)
    void saveChat(AIChat chat);

}
