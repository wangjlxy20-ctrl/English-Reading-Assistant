<template>
  <div class="app">
    <!-- Top Nav -->
    <header class="topbar">
      <span class="brand">📖 English Reading Assistant</span>
      <div class="nav-actions">
        <button class="btn-ghost" @click="currentPage = 'bookshelf'">Bookshelf</button>
        <label class="btn-ghost upload-label">
          Upload Book
          <input type="file" @change="uploadBook" hidden />
        </label>
        <button class="btn-ghost" @click="loadVocabulary()">Vocabulary</button>
      </div>
    </header>

    <!-- Bookshelf -->
    <div class="main-content" v-if="currentPage === 'bookshelf'">

      <!-- Book List -->
      <div v-if="!selectedBook" class="panel-center">
        <h2>My Bookshelf</h2>
        <div class="book-grid">
          <div v-for="book in books" :key="book.id" class="book-card" @click="loadChapters(book)">
            <div class="book-icon">📚</div>
            <strong>{{ book.title }}</strong>
            <div class="book-meta" v-if="getLastReadChapter(book.id)">
              Last read: Ch.{{ getLastReadChapter(book.id) }}
            </div>
            <button v-if="getLastReadChapter(book.id)" class="btn-primary small"
              @click.stop="continueReading(book)">Continue →</button>
          </div>
        </div>
      </div>

      <!-- Chapter List -->
      <div v-else-if="!selectedChapter" class="panel-center">
        <button class="btn-back" @click="selectedBook = null">← Back</button>
        <h2>{{ selectedBook.title }}</h2>
        <ul class="chapter-list">
          <li v-for="chapter in chapters" :key="chapter.id" @click="loadChapter(chapter)">
            <span class="ch-no">{{ chapter.chapterNo }}</span>
            {{ chapter.title }}
          </li>
        </ul>
      </div>

      <!-- ===== Reading Page: Left/Right Split Layout ===== -->
      <div v-else class="reading-layout">

        <!-- Left: Article -->
        <div class="reading-left">
          <div class="panel-header">
            <button class="btn-back" @click="selectedChapter = null">← Chapters</button>
            <h2>{{ selectedChapter.title }}</h2>
          </div>

          <div class="article-body" @mouseup.stop="handleSelection" @selectstart.stop>
            <p v-for="(sentence, idx) in sentences" :key="idx" class="sentence-block"
              :class="{ 'sentence-highlighted': isSentenceHighlighted(sentence) }">
              <!--
                KEY FIX #1: Render words inline with natural word-spacing.
                We use a data-word attribute to preserve the word for click,
                but CSS letter-spacing / word-spacing instead of &nbsp; tokens.
                word count still works because handleSelection uses text.split(/\s+/)
                on the browser selection string — which naturally has spaces.
              -->
              <span v-for="(token, ti) in tokenize(sentence)" :key="ti"
                :class="token.type === 'word' ? 'word-token' : 'punct-token'"
                @click.stop="token.type === 'word' && openWordMenu(token.value)">{{ token.value }}</span>
            </p>
          </div>
        </div>

        <!-- Right: AI Panel -->
        <div class="reading-right">
          <div class="ai-sidebar">

            <!-- Word Menu -->
            <transition name="fade">
              <div v-if="showMenu" class="ai-card word-menu-card">
                <div class="ai-card-title">
                  <span>🔤 {{ currentWord }}</span>
                  <button class="icon-btn" @click="showMenu = false">✕</button>
                </div>
                <div class="menu-btns">
                  <button class="btn-primary" @click="saveCurrentWord">⭐ Collect</button>
                  <button class="btn-primary" @click="explainCurrentWord">🤖 AI Explain</button>
                </div>
              </div>
            </transition>

            <!-- AI Word Explanation -->
            <transition name="fade">
              <div v-if="showAiPanel" class="ai-card">
                <div class="ai-card-title">
                  <span>💡 {{ selectedAiWord }}</span>
                  <button class="icon-btn" @click="closeAiPanel">✕</button>
                </div>
                <div class="ai-card-body" v-if="aiLoading">
                  <div class="loading-dots"><span></span><span></span><span></span></div>
                </div>
                <div class="ai-card-body formatted-text" v-else>
                  <div v-for="(block, i) in parseAiText(aiExplanation)" :key="i"
                    :class="block.type === 'heading' ? 'ai-heading' : 'ai-paragraph'">{{ block.text }}</div>
                </div>
              </div>
            </transition>

            <!-- Sentence Analysis -->
            <transition name="fade">
              <div v-if="showSentencePanel" class="ai-card">
                <div class="ai-card-title">
                  <span>📝 Sentence Analysis</span>
                  <button class="icon-btn" @click="closeSentencePanel">✕</button>
                </div>
                <div class="ai-card-body" v-if="sentenceLoading">
                  <div class="loading-dots"><span></span><span></span><span></span></div>
                </div>
                <div class="ai-card-body" v-else-if="sentenceAnalysis">
                  <div class="sa-sentence">{{ sentenceAnalysis.sentence }}</div>

                  <div class="sa-section">
                    <div class="sa-label">🌐 Translation</div>
                    <div class="sa-content">{{ sentenceAnalysis.meaning }}</div>
                  </div>

                  <div class="sa-section" v-if="sentenceAnalysis.keyPhrases?.length">
                    <div class="sa-label">🔑 Key Phrases</div>
                    <div class="tag-group">
                      <span v-for="p in sentenceAnalysis.keyPhrases" :key="p" class="tag">{{ p }}</span>
                    </div>
                  </div>

                  <div class="sa-section" v-if="sentenceAnalysis.grammar?.length">
                    <div class="sa-label">📐 Grammar</div>
                    <ul class="sa-list">
                      <li v-for="g in sentenceAnalysis.grammar" :key="g">{{ g }}</li>
                    </ul>
                  </div>

                  <div class="sa-section">
                    <div class="sa-label">⚡ Difficulty</div>
                    <div class="difficulty-bar">
                      <span v-for="n in 5" :key="n" class="diff-dot"
                        :class="{ active: n <= sentenceAnalysis.difficulty }"></span>
                      <span class="diff-label">{{ sentenceAnalysis.difficulty }}/5</span>
                    </div>
                  </div>
                </div>
              </div>
            </transition>

            <!-- Empty state -->
            <div v-if="!showMenu && !showAiPanel && !showSentencePanel" class="ai-empty">
              <div class="ai-empty-icon">💬</div>
              <p>Click a word to look it up,<br>or drag to select a sentence for analysis.</p>
            </div>

          </div>
        </div>
      </div>
    </div>

    <!-- Vocabulary Page -->
    <div class="main-content" v-else-if="currentPage === 'vocabulary'">
      <div class="panel-center">
        <h2>My Vocabulary</h2>
        <div class="vocab-grid">
          <div v-for="item in vocabulary" :key="item.id" class="vocab-card" @click="showWordDetail(item)"
            :class="{ active: selectedWord?.id === item.id }">
            <span class="vocab-word">{{ item.word }}</span>
            <button class="icon-btn danger" @click.stop="deleteWord(item.id)">🗑</button>
          </div>
        </div>

        <transition name="fade">
          <div v-if="selectedWord" class="word-detail-card">
            <h3>{{ selectedWord.word }}</h3>
            <div class="detail-row"><strong>Meaning:</strong> {{ dictionaryMeaning }}</div>
            <div class="detail-row"><strong>Example:</strong> <em>{{ dictionaryExample }}</em></div>
          </div>
        </transition>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import request from "./api/request";

const books = ref([]);
const chapters = ref([]);
const selectedBook = ref(null);
const selectedChapter = ref(null);
const words = ref([]);
const currentPage = ref("bookshelf");
const vocabulary = ref([]);
const selectedWord = ref(null);
const dictionaryMeaning = ref("");
const dictionaryExample = ref("");
const aiExplanation = ref("");
const selectedAiWord = ref("");
const readingRecords = ref([]);
const currentWord = ref("");
const showMenu = ref(false);
const showAiPanel = ref(false);
const aiLoading = ref(false);
const sentenceLoading = ref(false);
const sentences = ref([]);
const showSentencePanel = ref(false);
const sentenceAnalysis = ref(null);
const currentSentence = ref("");
let lastSelectedText = "";
const selectedSentence = ref("");

onMounted(async () => {
  await Promise.all([loadBooks(), loadRecords()]);
});

// ─── FIX #1: tokenize returns typed tokens, not flat strings ─────────────────
// This preserves word-count accuracy: when user selects text in the browser,
// the native Selection API returns text with single spaces between words,
// so split(/\s+/) counts correctly — even though we no longer inject &nbsp;
function tokenize(text) {
  const raw = text.match(/[A-Za-z]+(?:'[A-Za-z]+)?|[.,!?;:()"'\-]/g) || [];
  const result = [];
  for (let i = 0; i < raw.length; i++) {
    const val = raw[i];
    const isWord = /[A-Za-z]/.test(val);
    result.push({ type: isWord ? 'word' : 'punct', value: val });
    // Add a space after every token except: before punctuation, or at the end
    const next = raw[i + 1];
    if (next && /[.,!?;:)"'\-]/.test(next)) continue; // no space before punct
    if (i < raw.length - 1) result.push({ type: 'space', value: ' ' });
  }
  return result;
}

// ─── FIX #3: Highlight — compare by normalized text ─────────────────────────
function isSentenceHighlighted(sentence) {
  if (!selectedSentence.value) return false;
  const norm = (s) => s.replace(/\s+/g, ' ').trim().toLowerCase();
  return norm(sentence).includes(norm(selectedSentence.value)) ||
    norm(selectedSentence.value).includes(norm(sentence));
}

function splitSentences(text) {
  return text
    .replace(/\r\n/g, "\n")
    .split(/\n\s*\n|(?<=[.!?])\s+/)
    .map((s) => s.trim())
    .filter((s) => s.length > 0);
}

// ─── FIX #3: Selection handler ───────────────────────────────────────────────
async function handleSelection() {
  const selection = window.getSelection();
  const text = selection?.toString().trim();
  if (!text || text === lastSelectedText) return;

  // word count: split on whitespace — works correctly because browser gives
  // natural text even though we render tokens without &nbsp;
  const wordCount = text.split(/\s+/).filter(w => /[A-Za-z]/.test(w)).length;

  if (wordCount < 3) {
    // single-word click — let the word click handler manage it
    return;
  }

  selectedSentence.value = text;
  lastSelectedText = text;
  showMenu.value = false;

  console.log("Selected:", text, "| words:", wordCount);

  // Keep highlight visible while loading
  await analyzeSentence(text);

  // Clear browser selection after analysis is triggered
  selection.removeAllRanges();
}

// ─── Parse AI formatted text for display ────────────────────────────────────
function parseAiText(text) {
  if (!text) return [];
  return text.split('\n').filter(l => l.trim()).map(line => ({
    type: line.startsWith('【') ? 'heading' : 'paragraph',
    text: line
  }));
}

// ─── Navigation ──────────────────────────────────────────────────────────────
async function loadChapters(book) {
  selectedBook.value = book;
  const response = await request.get(`/chapters/book/${book.id}`);
  chapters.value = response.data;
}

async function loadChapter(chapter) {
  const response = await request.get(`/chapters/${chapter.id}`);
  selectedChapter.value = response.data;
  words.value = tokenize(response.data.content);
  sentences.value = splitSentences(response.data.content);
  await request.post("/records", {
    userId: 1,
    bookId: selectedBook.value.id,
    chapterId: chapter.id,
    progress: 100,
  });
}

async function continueReading(book) {
  const record = readingRecords.value.find((r) => r.bookId === book.id);
  if (!record) { alert("No reading record found"); return; }
  const response = await request.get(`/chapters/${record.chapterId}`);
  selectedBook.value = book;
  selectedChapter.value = response.data;
  words.value = tokenize(response.data.content);
  sentences.value = splitSentences(response.data.content);
}

// ─── Word actions ─────────────────────────────────────────────────────────────
function openWordMenu(word) {
  if (!word) return;
  currentWord.value = word;
  showMenu.value = true;
  // Close other panels when opening word menu
  showAiPanel.value = false;
}

async function saveCurrentWord() {
  await saveWord(currentWord.value);
}

async function explainCurrentWord() {
  showMenu.value = false;
  showAiPanel.value = true;
  aiLoading.value = true;
  await explainWord(currentWord.value);
  aiLoading.value = false;
}

function closeAiPanel() {
  showAiPanel.value = false;
  aiExplanation.value = "";
  selectedAiWord.value = "";
}

async function saveWord(word) {
  const response = await request.post("/words", {
    userId: 1,
    chapterId: selectedChapter.value.id,
    word: word,
    meaning: "To Be supplemented",
    example: "",
  });
  if (response.data === "saved") alert(word + " saved!");
  else if (response.data === "already exists") alert(word + " already collected!");
}

async function explainWord(word) {
  try {
    selectedAiWord.value = word;
    const response = await request.post("/ai/word", { word });
    aiExplanation.value = response.data;
  } catch {
    aiExplanation.value = "AI analysis failed";
  }
}

// ─── Sentence analysis ────────────────────────────────────────────────────────
async function analyzeSentence(sentence) {
  currentSentence.value = sentence;
  showSentencePanel.value = true;
  sentenceLoading.value = true;
  try {
    const res = await request.post("/sentence/analyze", {
      userId: 1,
      bookId: selectedBook.value.id,
      chapterId: selectedChapter.value.id,
      sentence,
    });
    sentenceAnalysis.value = res.data;
  } catch (e) {
    console.error(e);
  } finally {
    sentenceLoading.value = false;
  }
}

function closeSentencePanel() {
  showSentencePanel.value = false;
  sentenceAnalysis.value = null;
  selectedSentence.value = "";
  lastSelectedText = "";
}

// ─── Vocabulary ───────────────────────────────────────────────────────────────
async function loadVocabulary() {
  currentPage.value = "vocabulary";
  const response = await request.get("/words/1");
  vocabulary.value = response.data;
}

async function deleteWord(id) {
  await request.delete(`/words/${id}`);
  await loadVocabulary();
}

async function showWordDetail(item) {
  selectedWord.value = item;
  if (item.meaning && item.meaning !== "To Be supplemented") {
    dictionaryMeaning.value = item.meaning;
    dictionaryExample.value = item.example;
    return;
  }
  await lookupWord(item.word, item);
}

async function lookupWord(word, item) {
  try {
    const response = await axios.get(`https://api.dictionaryapi.dev/api/v2/entries/en/${word}`);
    const meanings = response.data[0].meanings;
    dictionaryMeaning.value = meanings[0].definitions[0].definition;
    let example = null;
    for (const m of meanings) {
      for (const d of m.definitions) {
        if (d.example) { example = d.example; break; }
      }
      if (example) break;
    }
    dictionaryExample.value = example || "No example available";
    await request.put(`/words/${item.id}`, { meaning: dictionaryMeaning.value, example: dictionaryExample.value });
    item.meaning = dictionaryMeaning.value;
    item.example = dictionaryExample.value;
  } catch {
    dictionaryMeaning.value = "Definition not found";
    dictionaryExample.value = "No example available";
  }
}

// ─── Book upload ──────────────────────────────────────────────────────────────
async function uploadBook(event) {
  const file = event.target.files[0];
  if (!file) return;
  const formData = new FormData();
  formData.append("file", file);
  try {
    await request.post("/books/upload", formData, { headers: { "Content-Type": "multipart/form-data" } });
    await loadBooks();
  } catch (err) {
    console.error("upload failed", err);
  }
}

async function loadBooks() {
  const res = await request.get("/books/list");
  books.value = res.data;
}

async function loadRecords() {
  const res = await request.get("/records/1");
  readingRecords.value = res.data;
}

function getLastReadChapter(bookId) {
  const record = readingRecords.value.find((r) => r.bookId === bookId);
  return record ? record.chapterId : null;
}
</script>

<style scoped>
/* ── Reset & Base ─────────────────────────────────────────────────────────── */
* {
  box-sizing: border-box;
}

.app {
  min-height: 100vh;
  background: #f8f7f4;
  font-family: 'Georgia', serif;
  color: #2c2c2c;
}

/* ── Topbar ───────────────────────────────────────────────────────────────── */
.topbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 32px;
  background: #1a1a2e;
  color: #e8e4d9;
  position: sticky;
  top: 0;
  z-index: 100;
}

.brand {
  font-size: 1.15rem;
  font-weight: bold;
  letter-spacing: 0.02em;
}

.nav-actions {
  display: flex;
  gap: 12px;
  align-items: center;
}

.btn-ghost {
  background: transparent;
  border: 1px solid rgba(255, 255, 255, 0.3);
  color: #e8e4d9;
  padding: 6px 14px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: background 0.2s;
}

.btn-ghost:hover {
  background: rgba(255, 255, 255, 0.1);
}

.upload-label {
  cursor: pointer;
}

/* ── Main Content ─────────────────────────────────────────────────────────── */
.main-content {
  padding: 24px 32px;
}

.panel-center {
  max-width: 860px;
  margin: 0 auto;
}

/* ── Bookshelf Grid ───────────────────────────────────────────────────────── */
.book-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 16px;
  margin-top: 16px;
}

.book-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.07);
  cursor: pointer;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  transition: transform 0.15s, box-shadow 0.15s;
  text-align: center;
}

.book-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.12);
}

.book-icon {
  font-size: 2rem;
}

.book-meta {
  font-size: 0.78rem;
  color: #888;
}

/* ── Chapter List ─────────────────────────────────────────────────────────── */
.chapter-list {
  list-style: none;
  padding: 0;
  margin: 16px 0;
}

.chapter-list li {
  padding: 12px 16px;
  border-radius: 8px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 12px;
  transition: background 0.15s;
}

.chapter-list li:hover {
  background: #eee;
}

.ch-no {
  display: inline-block;
  width: 28px;
  height: 28px;
  line-height: 28px;
  text-align: center;
  background: #1a1a2e;
  color: #fff;
  border-radius: 50%;
  font-size: 0.78rem;
  flex-shrink: 0;
}

/* ── Buttons ──────────────────────────────────────────────────────────────── */
.btn-back {
  background: none;
  border: none;
  color: #555;
  cursor: pointer;
  font-size: 0.9rem;
  padding: 4px 0;
  margin-bottom: 8px;
}

.btn-back:hover {
  color: #1a1a2e;
}

.btn-primary {
  background: #1a1a2e;
  color: #fff;
  border: none;
  border-radius: 6px;
  padding: 8px 16px;
  cursor: pointer;
  font-size: 0.85rem;
  transition: opacity 0.2s;
}

.btn-primary:hover {
  opacity: 0.85;
}

.btn-primary.small {
  padding: 5px 10px;
  font-size: 0.78rem;
}

.icon-btn {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1rem;
  color: #888;
  padding: 2px 4px;
  border-radius: 4px;
}

.icon-btn:hover {
  color: #333;
  background: #eee;
}

.icon-btn.danger:hover {
  color: #c0392b;
  background: #ffeaea;
}

/* ─────────────────────────────────────────────────────────────────────────── */
/* ── Reading Layout: LEFT / RIGHT split ─────────────────────────────────── */
/* ─────────────────────────────────────────────────────────────────────────── */
.reading-layout {
  display: flex;
  gap: 0;
  height: calc(100vh - 80px);
  /* subtract topbar */
  overflow: hidden;
}

.reading-left {
  flex: 1 1 60%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  border-right: 1px solid #e0ddd6;
  padding-right: 0;
}

.panel-header {
  padding: 16px 24px 8px;
  border-bottom: 1px solid #e8e4da;
  background: #f8f7f4;
  flex-shrink: 0;
}

.panel-header h2 {
  margin: 4px 0 0;
  font-size: 1.1rem;
  font-weight: 700;
}

/* ── FIX #1: Article body — natural typography, no big gaps ─────────────── */
.article-body {
  flex: 1;
  overflow-y: auto;
  padding: 24px 32px;
  line-height: 1.85;
  font-size: 1.05rem;
  color: #2c2c2c;

  /* Let browser handle text selection normally */
  user-select: text;
  -webkit-user-select: text;
  cursor: text;
}

/* Each sentence is an inline paragraph — they flow naturally */
.sentence-block {
  display: inline;
  margin: 0;
  padding: 2px 0;
  border-radius: 4px;
  transition: background 0.25s;
}

/* Add a small gap between sentences so they don't run together */
.sentence-block::after {
  content: ' ';
  white-space: pre;
}

/* ── FIX #3: Highlight on drag-selected sentence ──────────────────────────── */
.sentence-block.sentence-highlighted {
  background: #fff176;
  border-radius: 3px;
}

/* ── FIX #1: Word tokens — NO margin/padding, use natural font spacing ──── */
.word-token {
  cursor: pointer;
  border-radius: 3px;
  transition: background 0.15s;
  /* Natural character spacing — no extra margin */
}

.word-token:hover {
  background: #d4edff;
  text-decoration: underline;
  text-decoration-style: dotted;
  text-decoration-color: #5aabdf;
}

.punct-token {
  /* punctuation: no hover, no cursor */
  cursor: text;
}

/* ── Right sidebar ─────────────────────────────────────────────────────────── */
.reading-right {
  flex: 0 0 38%;
  max-width: 420px;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.ai-sidebar {
  flex: 1;
  overflow-y: auto;
  padding: 20px 20px 20px 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

/* ── AI Card ─────────────────────────────────────────────────────────────── */
.ai-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  /* FIX #2: content never overflows card */
  min-width: 0;
}

.ai-card-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  background: #1a1a2e;
  color: #e8e4d9;
  font-weight: bold;
  font-size: 0.9rem;
}

.ai-card-body {
  padding: 16px;
  font-size: 0.88rem;
  line-height: 1.7;
  /* FIX #2: wrap long text, no overflow */
  overflow-wrap: break-word;
  word-break: break-word;
  white-space: pre-wrap;
  max-height: 420px;
  overflow-y: auto;
}

/* Formatted AI text (word explanation) */
.formatted-text {
  white-space: normal;
}

.ai-heading {
  font-weight: bold;
  color: #1a1a2e;
  margin: 10px 0 4px;
  font-size: 0.9rem;
}

.ai-paragraph {
  color: #444;
  margin: 0 0 6px;
  padding-left: 4px;
}

/* Word menu card */
.word-menu-card .ai-card-body {
  display: none;
}

.menu-btns {
  display: flex;
  gap: 10px;
  padding: 14px 16px;
}

/* ── Sentence Analysis Card ──────────────────────────────────────────────── */
.sa-sentence {
  font-style: italic;
  color: #555;
  border-left: 3px solid #1a1a2e;
  padding-left: 10px;
  margin-bottom: 14px;
  font-size: 0.85rem;
  line-height: 1.5;
  overflow-wrap: break-word;
}

.sa-section {
  margin-bottom: 12px;
}

.sa-label {
  font-weight: bold;
  font-size: 0.78rem;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  color: #888;
  margin-bottom: 4px;
}

.sa-content {
  color: #2c2c2c;
  font-size: 0.88rem;
}

.sa-list {
  padding-left: 18px;
  margin: 0;
  color: #444;
  font-size: 0.85rem;
}

.sa-list li {
  margin-bottom: 3px;
}

.tag-group {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.tag {
  background: #eef4ff;
  color: #1a4fa0;
  border-radius: 4px;
  padding: 3px 8px;
  font-size: 0.8rem;
}

.difficulty-bar {
  display: flex;
  align-items: center;
  gap: 5px;
}

.diff-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background: #ddd;
}

.diff-dot.active {
  background: #1a1a2e;
}

.diff-label {
  font-size: 0.8rem;
  color: #888;
  margin-left: 4px;
}

/* ── Empty state ─────────────────────────────────────────────────────────── */
.ai-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  flex: 1;
  text-align: center;
  color: #aaa;
  padding: 40px 20px;
  font-size: 0.9rem;
  line-height: 1.7;
}

.ai-empty-icon {
  font-size: 2.5rem;
  margin-bottom: 12px;
}

/* ── Loading ─────────────────────────────────────────────────────────────── */
.loading-dots {
  display: flex;
  gap: 6px;
  justify-content: center;
  padding: 20px;
}

.loading-dots span {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #1a1a2e;
  animation: bounce 0.9s infinite ease-in-out;
}

.loading-dots span:nth-child(2) {
  animation-delay: 0.2s;
}

.loading-dots span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes bounce {

  0%,
  80%,
  100% {
    transform: scale(0.6);
    opacity: 0.4;
  }

  40% {
    transform: scale(1);
    opacity: 1;
  }
}

/* ── Vocabulary ──────────────────────────────────────────────────────────── */
.vocab-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 12px;
}

.vocab-card {
  background: #fff;
  border: 1px solid #e0ddd6;
  border-radius: 8px;
  padding: 8px 14px;
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  transition: background 0.15s;
}

.vocab-card:hover,
.vocab-card.active {
  background: #eef4ff;
  border-color: #1a4fa0;
}

.vocab-word {
  font-weight: bold;
  font-size: 0.95rem;
}

.word-detail-card {
  margin-top: 20px;
  background: #fff;
  border-radius: 12px;
  padding: 20px 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.word-detail-card h3 {
  margin: 0 0 12px;
  font-size: 1.2rem;
}

.detail-row {
  margin-bottom: 8px;
  font-size: 0.9rem;
  line-height: 1.6;
}

/* ── Transition ──────────────────────────────────────────────────────────── */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s, transform 0.2s;
}

.fade-enter-from {
  opacity: 0;
  transform: translateY(8px);
}

.fade-leave-to {
  opacity: 0;
}
</style>
