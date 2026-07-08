package com.wjl.englishreadingassistant.redis.util;

public class RedisKey {

    private RedisKey() {}

    public static String reading(Long userId, Long bookId) {
        return "reading:" + userId + ":" + bookId;
    }

    public static String chapter(Long chapterId) {
        return "chapter:" + chapterId;
    }

    public static String rag(String question) {
        return "rag" + question.hashCode();
    }


    public static final long CHAPTER_CACHE_MINUTES = 360;

}
