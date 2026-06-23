package com.wjl.englishreadingassistant.entity;


import lombok.Data;

@Data
public class Chapter {
    private Long id;

    private Long bookId;

    private Integer chapterNo;

    private String title;

    private String content;

}
