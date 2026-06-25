package com.wjl.englishreadingassistant.service.impl;

import com.wjl.englishreadingassistant.entity.Book;
import com.wjl.englishreadingassistant.entity.Chapter;
import com.wjl.englishreadingassistant.mapper.BookMapper;
import com.wjl.englishreadingassistant.mapper.ChapterMapper;
import com.wjl.englishreadingassistant.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookMapper bookMapper;
    private final ChapterMapper chapterMapper;
    public BookServiceImpl(BookMapper bookMapper, ChapterMapper chapterMapper) {
        this.bookMapper = bookMapper;
        this.chapterMapper = chapterMapper;
    }


    @Override
    public List<Book> findAll() {
        return bookMapper.findAll();
    }

    @Override
    public void importTxt(MultipartFile file) {

        try {

            String content =
                    new String(
                            file.getBytes(),
                            StandardCharsets.UTF_8
                    );
            String fileName =
                    file.getOriginalFilename();
            Book book = new Book();
            book.setTitle(fileName);
            book.setTotalChapters(1);
            bookMapper.insert(book);

            Chapter chapter = new Chapter();
            chapter.setBookId(book.getId());
            chapter.setChapterNo(1);
            chapter.setTitle("Chapter 1");
            chapter.setContent(content);
            chapterMapper.insert(chapter);

        } catch (IOException e) {
            throw new RuntimeException("Import Failed",e);
        }


    }






}
