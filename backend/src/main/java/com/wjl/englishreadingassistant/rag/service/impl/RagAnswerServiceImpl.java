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
            return new RagAnswer("Sorry,I could not locate any related information in the provided materials.", chunks);
        }

        String context = chunks.stream()
                .map(c -> "Text Chunk\n" + c.getContent())
                .collect(Collectors.joining("\n\n---\n\n"));

        String systemPrompt = """
                You are an English reading assistant.Please answer the user's question solely based on the reference excerpts provided below.
                If the reference excerpts do not contain sufficient information to answer a question,simply state "This question cannot be answered based on the available materials" and do not fabricate any content.
                Respond in concise and clear Chinese; you may quote key sentences from the excerpts when necessary.
                """;

        String userPrompt = "Reference chunks:\n" + context + "\n\nUser question :" + question;

        String answerText = chatClient.chat(systemPrompt, userPrompt);

        return new RagAnswer(answerText, chunks);
    }
}
