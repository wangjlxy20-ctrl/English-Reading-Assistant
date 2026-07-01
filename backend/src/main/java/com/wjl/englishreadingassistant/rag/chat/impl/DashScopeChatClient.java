package com.wjl.englishreadingassistant.rag.chat.impl;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.wjl.englishreadingassistant.rag.chat.ChatClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DashScopeChatClient implements ChatClient {

    @Value("${tongyi.api.key}")
    private String apiKey;

    @Value("${tongyi.chat-model}")
    private String chatModel;

    private OpenAIClient client;

    @PostConstruct
    public void init() {
        client = OpenAIOkHttpClient.builder()
                .apiKey(apiKey)
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .build();
    }

    @Override
    public String chat(String systemPrompt, String userPrompt) {

        ChatCompletionCreateParams params =
                ChatCompletionCreateParams.builder()
                        .model(chatModel)
                        .addSystemMessage(systemPrompt)
                        .addUserMessage(userPrompt)
                        .build();

        ChatCompletion completion = client.chat().completions().create(params);

        return completion.choices()
                .get(0)
                .message()
                .content()
                .orElse("");
    }
}