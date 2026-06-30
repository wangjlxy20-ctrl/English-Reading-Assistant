package com.wjl.englishreadingassistant.rag.embedding;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.embeddings.CreateEmbeddingResponse;
import com.openai.models.embeddings.EmbeddingCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashScopeEmbeddingClient implements EmbeddingClient{

    @Value("${tongyi.api.key}")
    private String apiKey;

    @Value("${tongyi.embedding-model}")
    private String embeddingModel;

    private OpenAIClient client;

    @PostConstruct
    public void init() {
        client = OpenAIOkHttpClient.builder()
                        .apiKey(apiKey)
                        .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                        .build();
    }

    @Override
    public List<Float> embed(String text) {
    EmbeddingCreateParams params =
        EmbeddingCreateParams.builder()
                .model(embeddingModel)
                .input(
                        EmbeddingCreateParams.Input.ofString(text)
                )
                .dimensions(1024)
                .build();

        CreateEmbeddingResponse response =
                client.embeddings().create(params);

        List<Float> embedding = response.data().get(0).embedding();

        System.out.println(response.data().get(0).embedding());
        System.out.println("Embedding Vector dimension :" + embedding.size());
        return embedding;
    }
}
