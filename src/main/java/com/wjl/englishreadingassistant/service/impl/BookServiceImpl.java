package com.wjl.englishreadingassistant.service.impl;

import com.wjl.englishreadingassistant.entity.Book;
import com.wjl.englishreadingassistant.mapper.BookMapper;
import com.wjl.englishreadingassistant.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookMapper bookMapper;

    public BookServiceImpl(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    @Override
    public List<Book> findAll() {
        return bookMapper.findAll();
    }
}
