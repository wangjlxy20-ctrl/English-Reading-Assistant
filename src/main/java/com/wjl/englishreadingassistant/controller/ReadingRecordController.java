package com.wjl.englishreadingassistant.controller;

import com.wjl.englishreadingassistant.entity.ReadingRecord;
import com.wjl.englishreadingassistant.service.ReadingRecordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/records")
public class ReadingRecordController {
    private final ReadingRecordService service;


    public ReadingRecordController(ReadingRecordService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public List<ReadingRecord> list(
            @PathVariable Long userId){
                return service.findByUserId(userId);
    }

    @PostMapping
    public String save(
            @RequestBody ReadingRecord record){
        service.save(record);
        return "success";
    }


}
