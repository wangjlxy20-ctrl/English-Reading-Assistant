package com.wjl.englishreadingassistant.controller;

import com.wjl.englishreadingassistant.entity.Book;
import com.wjl.englishreadingassistant.service.BookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> list(){
        return bookService.findAll();
    }
}
