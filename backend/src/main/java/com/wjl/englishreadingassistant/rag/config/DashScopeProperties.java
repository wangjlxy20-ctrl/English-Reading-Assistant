package com.wjl.englishreadingassistant.rag.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "dashscope")
@Configuration
public class DashScopeProperties {

    private String apiKey;
    private String embeddingModel;
}
