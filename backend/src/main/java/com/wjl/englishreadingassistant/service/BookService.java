package com.wjl.englishreadingassistant.service;
import com.wjl.englishreadingassistant.entity.Book;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {
    List<Book> findAll();

    void importTxt(MultipartFile file);
}
