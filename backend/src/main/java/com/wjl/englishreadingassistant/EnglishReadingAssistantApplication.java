package com.wjl.englishreadingassistant;

import com.wjl.englishreadingassistant.rag.config.RagChunkProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RagChunkProperties.class)
public class EnglishReadingAssistantApplication {

    public static void main(String[] args) {

        SpringApplication.run(
                EnglishReadingAssistantApplication.class,
                args);
    }

}
