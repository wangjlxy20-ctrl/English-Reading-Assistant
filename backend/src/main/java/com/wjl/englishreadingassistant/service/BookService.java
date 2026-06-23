package com.wjl.englishreadingassistant.service;
import com.wjl.englishreadingassistant.entity.Book;
import java.util.List;

public interface BookService {
    List<Book> findAll();
}
