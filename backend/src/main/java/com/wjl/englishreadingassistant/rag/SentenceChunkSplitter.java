package com.wjl.englishreadingassistant.rag;


import com.wjl.englishreadingassistant.dto.ChunkResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SentenceChunkSplitter implements ChunkSplitter{
    //Manx 300 words in every single chunk
    private static final int MAX_WORDS = 300;

    @Override
    public List<ChunkResult> split(String content) {

        List<ChunkResult> chunks = new ArrayList<>();

        if (content == null || content.isBlank()) {
            return chunks;
        }

        //split by sentence
        String[] sentences = content.split("(?<=[.!?])\\s+");
        StringBuilder currentChunk = new StringBuilder();
        int currentWordCount = 0;

        for (String sentence : sentences) {
            //count the number of words in current sentence
            int wordCount = sentence.trim().split("\\s+").length;

            //Adding this sentence will exceed the maximum length
            if (currentWordCount + wordCount > MAX_WORDS) {

                //Save the current chunk
                /*!currentChunk.isEmpty() is used to prevent creating
                * an empty chunk
                * when the first
                * sentence exceeds 300 words
                * */
                if (!currentChunk.isEmpty()) {
                    chunks.add(
                            new ChunkResult(
                                currentChunk.toString().trim(),
                                currentWordCount
                            )
                    );
                }

                //Start a new chunk
                currentChunk.setLength(0);
                currentWordCount = 0;
            }

            //Append the current sentence to the chunk
            currentChunk.append(sentence).append(" ");
            currentWordCount += wordCount;

        }

            //Sava the last chunk
        if(!currentChunk.isEmpty()) {
            chunks.add(
                    new ChunkResult(
                            currentChunk.toString().trim(),
                            currentWordCount
                    )
            );
        }
        return chunks;
    }
}
