package com.wjl.englishreadingassistant.service.impl;

import com.wjl.englishreadingassistant.entity.Chapter;
import com.wjl.englishreadingassistant.mapper.ChapterMapper;
import com.wjl.englishreadingassistant.service.ChapterService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ChapterServiceImpl implements ChapterService {
    private final ChapterMapper chapterMapper;

    public ChapterServiceImpl(ChapterMapper chapterMapper) {
        this.chapterMapper = chapterMapper;
    }


    @Override
    public List<Chapter> findByBookId(Long bookId) {
        return chapterMapper.findByBookId(bookId);
    }

    @Override
    public Chapter findById(Long id) {
        return chapterMapper.findById(id);
    }

    @Override
    public List<Chapter> parseChapters(String content) {
        //TEST Parse
            String[] parts = content.split(
                    "(?i)(?=chapter\\s+\\d+|chapter\\s+[ivx]+|part\\s+[ivx]+)"
            );

            System.out.println("===== SPLIT RESULT =====");
            System.out.println("PART COUNT = " + parts.length);

            for (int i = 0; i < parts.length; i++) {
                System.out.println("----- PART " + (i+1) + " -----");
                System.out.println(parts[i].substring(0, Math.min(100, parts[i].length())));
            }

        return extractChapters(content);
    }


    private String extractChapterTitle(String text) {

        String firstLine = text.split("\n")[0];
        if (firstLine.length() > 50) {
            return "Chapter";
        }

        return firstLine.trim();
    }


    private List<Chapter> extractChapters(String content) {
        List<Chapter> chapters = new ArrayList<>();
        //Crude segmentation by markers:"Chapter 1","1","I","PART"
        String[] parts = content.split(
                "(?i)(?=chapter\\s+\\d+|chapter\\s+[ivx]+|part\\s+[ivx]+)"
        );
        int index = 1;

        for (String part : parts) {

            if(part.trim().isEmpty()) continue;
            Chapter chapter = new Chapter();
            chapter.setChapterNo(index++);
            chapter.setTitle(extractChapterTitle(part));
            chapter.setContent(part.trim());

            chapters.add(chapter);
        }

        return chapters;
    }




}
