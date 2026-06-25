package com.wjl.englishreadingassistant.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AIChat {

    private Long id;

    private Long userId;

    private String question;

    private String answer;

    private String context;

    private LocalDateTime createTime;

}
