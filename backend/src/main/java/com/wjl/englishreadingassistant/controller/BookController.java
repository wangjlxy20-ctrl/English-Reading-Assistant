package com.wjl.englishreadingassistant.controller;

import com.wjl.englishreadingassistant.entity.Book;
import com.wjl.englishreadingassistant.service.BookService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/books")
@CrossOrigin
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/list")
    public List<Book> list(){
        return bookService.findAll();
    }

    @PostMapping("/upload")
    public String upload(
            @RequestParam("file")
            MultipartFile file){

        String filename =
                file.getOriginalFilename();

        if(filename == null || !filename.endsWith(".txt")){
            throw new RuntimeException(
                    "Only TXT files are supported at present"
            );
        }

        String title =
                filename.substring(
                        0,
                        filename.lastIndexOf(".")
                );

        bookService.importTxt(file,title);

        return "success";
    }



}
