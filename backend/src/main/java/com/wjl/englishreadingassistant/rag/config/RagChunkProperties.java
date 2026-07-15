package com.wjl.englishreadingassistant.rag.config;


import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@ConfigurationProperties(prefix = "rag.chunk")
@Validated
public class RagChunkProperties {
    //Maximum allowed word quantity for a single text chunk
    @Min(50)
    private Integer maxWords = 300;

    //Number of overlapping words between two consecutive chunks
    @Min(0)
    private Integer overlap = 40;
}
