package com.wjl.englishreadingassistant.rag.chat;

public interface ChatClient {
    /*
     * generate  reply  based on system prompt + user prompt
     * */
    String chat(String systemPrompt, String userPrompt);
}
