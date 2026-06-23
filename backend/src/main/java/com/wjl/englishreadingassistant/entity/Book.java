package com.wjl.englishreadingassistant.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Book {
    private Long id;
    private String title;
    private String author;
    private String coverUrl;
    private String description;
    private Integer totalChapters;
    private LocalDateTime createTime;
}
