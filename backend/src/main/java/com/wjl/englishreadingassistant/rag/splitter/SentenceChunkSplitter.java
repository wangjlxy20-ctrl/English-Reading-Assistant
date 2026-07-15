package com.wjl.englishreadingassistant.rag.splitter;


import com.wjl.englishreadingassistant.rag.config.RagChunkProperties;
import com.wjl.englishreadingassistant.rag.dto.ChunkResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SentenceChunkSplitter implements ChunkSplitter {
    //Max 300 words in every single chunk
    private final RagChunkProperties properties;

    public SentenceChunkSplitter(RagChunkProperties properties) {
        this.properties = properties;
    }

    @Override
    public List<ChunkResult> split(String content) {

        List<ChunkResult> chunks = new ArrayList<>();

        if (content == null || content.isBlank()) {
            return chunks;
        }

        int maxWords = properties.getMaxWords();
        int overlap = properties.getOverlap();

        //split by sentence
        String[] sentences = content.split("(?<=[.!?])\\s+");

        // Keep sentences (not raw text) in the current chunk so we can
        // rebuild an overlap tail out of *whole* sentences afterward.
        List<String> currentSentences = new ArrayList<>();
        int currentWordCount = 0;

        for (String sentence : sentences) {
            //count the number of words in current sentence
            int wordCount = sentence.trim().split("\\s+").length;

            //Adding this sentence will exceed the maximum length
            if (currentWordCount + wordCount > maxWords) {

                /*!currentSentences.isEmpty() is used to prevent creating
                 * an empty chunk
                 * when the first
                 * sentence exceeds 300 words
                 * */
                if (!currentSentences.isEmpty()) {
                    chunks.add(
                            new ChunkResult(
                                    String.join(" ", currentSentences).trim(),
                                    currentWordCount
                            )
                    );

                    // Build the overlap tail: walk backwards from the end
                    // of the chunk we just closed, keeping whole sentences
                    // until we've collected `overlap` words. This becomes
                    // the opening of the next chunk, so consecutive chunks
                    // share context instead of hard-cutting mid-topic.
                    List<String> overlapSentences = new ArrayList<>();
                    int overlapWordCount = 0;
                    for (int i = currentSentences.size() - 1; i >= 0; i--) {
                        String s = currentSentences.get(i);
                        int sWordCount = s.trim().split("\\s+").length;
                        if (overlapWordCount + sWordCount > overlap) {
                            break;
                        }
                        overlapSentences.add(0, s);
                        overlapWordCount += sWordCount;
                    }

                    currentSentences = overlapSentences;
                    currentWordCount = overlapWordCount;
                } else {
                    //Start a new chunk (nothing to overlap from — the
                    //previous sentence alone already exceeded maxWords)
                    currentWordCount = 0;
                }
            }

            //Append the current sentence to the chunk
            currentSentences.add(sentence);
            currentWordCount += wordCount;

        }

        //Save the last chunk
        if (!currentSentences.isEmpty()) {
            chunks.add(
                    new ChunkResult(
                            String.join(" ", currentSentences).trim(),
                            currentWordCount
                    )
            );
        }
        return chunks;
    }
}