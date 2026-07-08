package com.wjl.englishreadingassistant.service.impl;

import com.wjl.englishreadingassistant.entity.Chapter;
import com.wjl.englishreadingassistant.mapper.ChapterMapper;
import com.wjl.englishreadingassistant.redis.service.RedisService;
import com.wjl.englishreadingassistant.redis.util.RedisKey;
import com.wjl.englishreadingassistant.service.ChapterService;
import org.springframework.stereotype.Service;
import com.wjl.englishreadingassistant.redis.util.RedisKey;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ChapterServiceImpl implements ChapterService {

    private final ChapterMapper chapterMapper;

    private final RedisService redisService;

    // ── 核心正则 ──────────────────────────────────────────────────────────────
    // 匹配章节标记行，格式举例：
    //   "Chapter 1"  "Chapter 2: The Journey"  "CHAPTER IV"
    //   "Part I"     "PART II - Second Act"
    // group(1) = kind (chapter/part)
    // group(2) = number (阿拉伯数字 或 罗马数字)
    // group(3) = 可选副标题（冒号/破折号之后，最多60字符）
    private static final Pattern CHAPTER_MARKER = Pattern.compile(
        "^(chapter|part)\\s+(\\d+|[ivxlcdmIVXLCDM]+)\\s*[:\\-\\u2013\\u2014]?\\s*(.{0,60})?$",
        Pattern.CASE_INSENSITIVE
    );

    // 一行中英文单词数超过此值 → 认为是正文句子，不是章节标题行
    private static final int MAX_TITLE_WORD_COUNT = 8;

    public ChapterServiceImpl(ChapterMapper chapterMapper, RedisService redisService) {
        this.chapterMapper = chapterMapper;
        this.redisService = redisService;
    }

    // ─────────────────────────────────────────────────────────────────────────
    @Override
    public List<Chapter> findByBookId(Long bookId) {
        return chapterMapper.findByBookId(bookId);
    }

    @Override
    public Chapter findById(Long id) {

        String key = RedisKey.chapter(id);
        Chapter cache =
                redisService.get(key,Chapter.class);

        if(cache != null){
            System.out.println("[Redis] Cache hit : " + key);
            return cache;
        }
        System.out.println("[Redis] Cache miss : " + key);

        Chapter chapter = chapterMapper.findById(id);

        if(chapter != null) {
            redisService.set(
                    key,
                    chapter,
                    Duration.ofMinutes(RedisKey.CHAPTER_CACHE_MINUTES)
            );
        }

        return chapter;
    }

    @Override
    public List<Chapter> parseChapters(String content) {
        List<Chapter> chapters = extractChapters(content);
        System.out.println("===== CHAPTER PARSE RESULT =====");
        System.out.println("Total chapters: " + chapters.size());
        for (Chapter c : chapters) {
            System.out.printf("  [%d] %s  (content len=%d)%n",
                c.getChapterNo(), c.getTitle(), c.getContent().length());
        }
        return chapters;
    }

    // ─────────────────────────────────────────────────────────────────────────
    /**
     * 主切割算法
     *
     * 步骤：
     *  1. 按行扫描，找出所有真正的"章节标记行"及其行号
     *  2. 对每个标记行做去重（相同 kind+编号 只保留第一次出现）
     *  3. 用标记行之间的行范围提取章节内容
     *  4. 如果全文没有任何标记，退化为整本书作为一章
     */
    private List<Chapter> extractChapters(String content) {

        String[] lines = content.replace("\r\n", "\n").replace("\r", "\n").split("\n");

        // Step 1: 扫描，收集候选标记行
        List<MarkerInfo> markers = new ArrayList<>();
        Set<String> seenKeys = new LinkedHashSet<>();   // 去重用

        for (int i = 0; i < lines.length; i++) {
            String trimmed = lines[i].trim();
            if (trimmed.isEmpty()) continue;

            // 单词数过多 → 正文句子，直接跳过
            int wordCount = trimmed.split("\\s+").length;
            if (wordCount > MAX_TITLE_WORD_COUNT) continue;

            Matcher m = CHAPTER_MARKER.matcher(trimmed);
            if (!m.matches()) continue;

            String kind    = m.group(1).toUpperCase();           // CHAPTER / PART
            String numRaw  = m.group(2);                         // 原始数字字符串
            String subRaw  = m.group(3) != null ? m.group(3).trim() : "";
            // 清理副标题中的前导标点
            String subtitle = subRaw.replaceAll("^[:\\-\\u2013\\u2014\\s.]+", "").trim();

            // 归一化编号（罗马→阿拉伯）用于去重 key
            int numNorm = normalizeNumber(numRaw);
            String dedupKey = kind + "-" + numNorm;

            // Step 2: 去重，同一章节编号只保留首次出现
            if (seenKeys.contains(dedupKey)) {
                System.out.printf("  [DEDUP] skip duplicate: %s %s (line %d)%n", kind, numRaw, i);
                continue;
            }
            seenKeys.add(dedupKey);

            // 构造显示标题
            String displayTitle = buildTitle(kind, numRaw, subtitle);

            markers.add(new MarkerInfo(i, displayTitle, kind, numNorm));
        }

        // Step 3: 没有找到任何章节标记 → 全文作为一章
        if (markers.isEmpty()) {
            Chapter single = new Chapter();
            single.setChapterNo(1);
            single.setTitle("Full Text");
            single.setContent(content.trim());
            return Collections.singletonList(single);
        }

        // Step 4: 按标记行切分内容
        List<Chapter> chapters = new ArrayList<>();
        int chapterNo = 1;

        for (int i = 0; i < markers.size(); i++) {
            MarkerInfo current = markers.get(i);
            int startLine = current.lineIndex;
            int endLine   = (i + 1 < markers.size())
                            ? markers.get(i + 1).lineIndex - 1
                            : lines.length - 1;

            // 从 startLine 到 endLine 拼接内容（包含标题行本身）
            StringBuilder sb = new StringBuilder();
            for (int li = startLine; li <= endLine; li++) {
                sb.append(lines[li]).append("\n");
            }

            String chapterContent = sb.toString().trim();

            // 跳过内容过短的（比如只有标题行，正文为空）
            // 这里用字符数判断，少于 20 字符认为是空章节
            if (chapterContent.length() < 20) {
                System.out.printf("  [SKIP] empty chapter: %s%n", current.title);
                continue;
            }

            Chapter chapter = new Chapter();
            chapter.setChapterNo(chapterNo++);
            chapter.setTitle(current.title);
            chapter.setContent(chapterContent);
            chapters.add(chapter);
        }

        return chapters;
    }

    // ─────────────────────────────────────────────────────────────────────────
    /**
     * 构造章节显示标题
     * "CHAPTER" + "5" + "The Journey" → "Chapter 5: The Journey"
     * "PART"    + "I" + ""            → "Part I"
     */
    private String buildTitle(String kind, String numRaw, String subtitle) {
        String kindDisplay = kind.substring(0, 1) + kind.substring(1).toLowerCase(); // Chapter / Part
        String base = kindDisplay + " " + numRaw.toUpperCase();
        if (!subtitle.isEmpty()) {
            return base + ": " + subtitle;
        }
        return base;
    }

    // ─────────────────────────────────────────────────────────────────────────
    /**
     * 将章节编号归一化为整数，用于去重比较
     * "5"   → 5
     * "IV"  → 4
     * "XII" → 12
     */
    private int normalizeNumber(String raw) {
        if (raw == null || raw.isEmpty()) return -1;
        // 纯数字
        if (raw.matches("\\d+")) {
            return Integer.parseInt(raw);
        }
        // 罗马数字
        try {
            return romanToInt(raw.toUpperCase());
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 罗马数字 → 整数
     * 支持 I II III IV V VI VII VIII IX X ... L C D M
     */
    private int romanToInt(String s) {
        Map<Character, Integer> vals = new LinkedHashMap<>();
        vals.put('I', 1);
        vals.put('V', 5);
        vals.put('X', 10);
        vals.put('L', 50);
        vals.put('C', 100);
        vals.put('D', 500);
        vals.put('M', 1000);

        int total = 0;
        int prev  = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            int cur = vals.getOrDefault(s.charAt(i), 0);
            if (cur < prev) total -= cur;
            else            total += cur;
            prev = cur;
        }
        return total;
    }

    // ─────────────────────────────────────────────────────────────────────────
    /** 内部数据类：记录一个章节标记的位置和元信息 */
    private static class MarkerInfo {
        final int    lineIndex;   // 在 lines[] 中的行号
        final String title;       // 最终显示标题
        final String kind;        // CHAPTER / PART
        final int    number;      // 归一化后的编号

        MarkerInfo(int lineIndex, String title, String kind, int number) {
            this.lineIndex = lineIndex;
            this.title     = title;
            this.kind      = kind;
            this.number    = number;
        }
    }
}
