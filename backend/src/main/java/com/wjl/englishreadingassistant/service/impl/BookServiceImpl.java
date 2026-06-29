package com.wjl.englishreadingassistant.service.impl;

import com.wjl.englishreadingassistant.entity.Book;
import com.wjl.englishreadingassistant.entity.Chapter;
import com.wjl.englishreadingassistant.mapper.BookMapper;
import com.wjl.englishreadingassistant.mapper.ChapterMapper;
import com.wjl.englishreadingassistant.service.BookService;
import com.wjl.englishreadingassistant.service.ChapterService;
import com.wjl.englishreadingassistant.service.ChunkService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookMapper bookMapper;
    private final ChapterMapper chapterMapper;
    private final ChapterService chapterService;
    private final ChunkService chunkService;
    public BookServiceImpl(BookMapper bookMapper, ChapterMapper chapterMapper, ChapterService chapterService, ChunkService chunkService) {
        this.bookMapper = bookMapper;
        this.chapterMapper = chapterMapper;
        this.chapterService = chapterService;
        this.chunkService = chunkService;
    }


    @Override
    public List<Book> findAll() {
        return bookMapper.findAll();
    }


    @Override
    @Transactional
    public void importTxt(MultipartFile file,String title) {

        try {

            String content =
                    new String(
                            file.getBytes(),
                            StandardCharsets.UTF_8
                    );

            //create book
            Book book = new Book();
            book.setTitle(title);
            book.setTotalChapters(0);
            bookMapper.insert(book);

            //parse chapters
            List<Chapter> chapters = chapterService
                        .parseChapters(content);

            book.setTotalChapters(chapters.size());
            //Update chapter count
            bookMapper.updateTotalChapters(book.getId(),chapters.size());


            //Save chapter and generate chunks
            for(Chapter chapter : chapters){
                //set affiliated book
                chapter.setBookId(book.getId());

                //save chapter
                chapterMapper.insert(chapter);

                //Test Point:Chapter_id = null???
                System.out.println("chapterId = " + chapter.getId());

                //perform automatic chunk segmentation
                //build knowledge base
                chunkService.generateChunks(chapter);
            }

        } catch (IOException e) {
            throw new RuntimeException("Import Failed",e);
        }


    }






}
