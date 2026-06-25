package com.wjl.englishreadingassistant.service;


import com.wjl.englishreadingassistant.entity.AIChat;
import com.wjl.englishreadingassistant.mapper.AIChatMapper;
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.GeneralGetParam;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Arrays;

@Service
public class AIService {

    private final AIChatMapper aiChatMapper;

    public AIService(AIChatMapper aiChatMapper) {
        this.aiChatMapper = aiChatMapper;
    }

    // read properties configuration directly, no need to create a new Config class
    @Value("${tongyi.api.key}")
    private String apiKey;

    @Value("${tongyi.api.url}")
    private String apiUrl;

    @Value("${tongyi.model}")
    private String model;

    @Value("${tongyi.temperature:0.3}")
    private Float temperature;

    @Value("${tongyi.max-token:2000}")
    private Integer maxToken;

    /**
     * Configure AI text processing method
     */
    public String askQwen(String englishText) {
        String systemPrompt = """
            You are an English original reading assistant.

            Your tasks:

            1. Explain the English text.
            2. Extract key vocabulary.
            3. Provide Chinese translation.
            4. Analyze grammar.
            5. Give easy-to-understand explanations.

            Please return in the following format:

            【Translation】
            ...

            【Vocabulary】
            ...

            【Grammar】
            ...

            【Explanation】
            ...
            """;


        try {
            Generation gen = new Generation();
            Message systemMsg = Message.builder()
                    .role(Role.SYSTEM.getValue())
                    .content(systemPrompt)
                    .build();
            Message userMsg = Message.builder()
                    .role(Role.USER.getValue())
                    .content(englishText)
                    .build();
            GenerationParam param = GenerationParam.builder()
                    .apiKey(apiKey)
                    .model(model)
                    .messages(Arrays.asList(systemMsg, userMsg))
                    .temperature(temperature)
                    .maxTokens(maxToken)
                    .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                    .build();
            GenerationResult result = gen.call(param);
            return result.getOutput().getChoices().get(0).getMessage().getContent();
        } catch (NoApiKeyException e) {
            return "Failed to call AI";
        } catch (ApiException e) {
            return "Failed to call AI interface: " + e.getMessage();
        } catch (InputRequiredException e) {
            return "Failed to call AI: Input text cannot be empty";
        }
    }

    public String analyzeWord(String word) {

        String systemPrompt = """
                You are an English reading assistant.
                
                Please explain the selected word or phrase.
                
                Output format:
                
                【Meaning】
                ...
                
                【Chinese】
                ...
                
                【Example】
                ...
                
                【Reading Notes】
                ...
                """;

            try {
                Generation gen = new Generation();

                Message systemMsg = Message.builder()
                        .role(Role.SYSTEM.getValue())
                        .content(systemPrompt)
                        .build();

                Message useMsg = Message.builder()
                        .role(Role.USER.getValue())
                        .content(word)
                        .build();

                GenerationParam param = GenerationParam.builder()
                        .apiKey(apiKey)
                        .model(model)
                        .messages(Arrays.asList(systemMsg,useMsg))
                        .temperature(temperature)
                        .maxTokens(maxToken)
                        .resultFormat(
                                GenerationParam.ResultFormat.MESSAGE
                        ).build();



                GenerationResult result = gen.call(param);

                String answer =
                        result.getOutput()
                                .getChoices()
                                .get(0)
                                .getMessage()
                                .getContent();

                AIChat  chat = new AIChat();
                chat.setUserId(1L);
                chat.setQuestion(word);
                chat.setAnswer(answer);
                chat.setContext("word_explanation");
                aiChatMapper.saveChat(chat);
                return answer;
            } catch (Exception e) {
                return "AI analysis failed";
            }
    }




}