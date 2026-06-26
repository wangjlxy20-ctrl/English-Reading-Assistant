package com.wjl.englishreadingassistant.util;

import java.util.Arrays;
import java.util.List;

public class SentenceSplitterUtil {

    public static List<String> split(String text) {
        return Arrays.stream(
                text.split("(?<=[.!?])\\s+"))
                .map(String::trim)
                .filter(s->!s.isEmpty())
                .toList();
    }


}
