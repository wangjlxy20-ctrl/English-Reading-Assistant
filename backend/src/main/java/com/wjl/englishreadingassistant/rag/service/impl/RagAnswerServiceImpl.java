package com.wjl.englishreadingassistant.rag.service.impl;

import com.wjl.englishreadingassistant.entity.Chunk;
import com.wjl.englishreadingassistant.rag.chat.ChatClient;
import com.wjl.englishreadingassistant.rag.dto.RagAnswer;
import com.wjl.englishreadingassistant.rag.service.RagAnswerService;
import com.wjl.englishreadingassistant.rag.service.RetrievalService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RagAnswerServiceImpl implements RagAnswerService {

    private final RetrievalService retrievalService;
    private final ChatClient chatClient;

    public RagAnswerServiceImpl(RetrievalService retrievalService, ChatClient chatClient) {
        this.retrievalService = retrievalService;
        this.chatClient = chatClient;
    }

    @Override
    public RagAnswer answer(String question) {

        List<Chunk> chunks = retrievalService.retrieve(question);

        if (chunks.isEmpty()) {
            return new RagAnswer("抱歉，没有在资料中找到相关内容。", chunks);
        }

        String context = chunks.stream()
                .map(c -> "【片段】\n" + c.getContent())
                .collect(Collectors.joining("\n\n---\n\n"));

        String systemPrompt = """
                你是一个英语阅读助手。请仅根据下面提供的参考片段回答用户问题。
                如果参考片段中没有足够信息回答问题，请直接说"根据现有资料无法回答这个问题"，不要编造内容。
                回答请使用简洁清晰的中文，必要时可以引用原文片段中的关键句子。
                """;

        String userPrompt = "参考片段：\n" + context + "\n\n用户问题：" + question;

        String answerText = chatClient.chat(systemPrompt, userPrompt);

        return new RagAnswer(answerText, chunks);
    }
}
