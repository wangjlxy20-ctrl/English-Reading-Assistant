<template>
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

  <!-- Reading page -->
  <ReadingPage v-else />
</template>

<script setup>
import ReadingPage from "./ReadingPage.vue";
import { useBooks } from "../composables/useBooks";

const {
  books,
  chapters,
  selectedBook,
  selectedChapter,
  loadChapters,
  loadChapter,
  continueReading,
  getLastReadChapter,
} = useBooks();
</script>

<style scoped>
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
</style>
