import { ref } from "vue";
import request from "../api/request";
import { useReading } from "./useReading";

// Module-scoped refs => singleton state shared by every component
// that calls useBooks(). This mirrors the original App.vue's
// top-level refs without needing Vuex/Pinia.
const books = ref([]);
const chapters = ref([]);
const selectedBook = ref(null);
const selectedChapter = ref(null);
const readingRecords = ref([]);

export function useBooks() {
  const { sentences, words, tokenize, splitSentences } = useReading();

  async function loadBooks() {
    const res = await request.get("/books/list");
    books.value = res.data;
  }

  async function loadRecords() {
    const res = await request.get("/records/1");
    readingRecords.value = res.data;
  }

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
    if (!record) {
      alert("No reading record found");
      return;
    }
    const response = await request.get(`/chapters/${record.chapterId}`);
    selectedBook.value = book;
    selectedChapter.value = response.data;
    words.value = tokenize(response.data.content);
    sentences.value = splitSentences(response.data.content);
  }

  async function uploadBook(event) {
    const file = event.target.files[0];
    if (!file) return;
    const formData = new FormData();
    formData.append("file", file);
    try {
      await request.post("/books/upload", formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });
      await loadBooks();
    } catch (err) {
      console.error("upload failed", err);
    }
  }

  function getLastReadChapter(bookId) {
    const record = readingRecords.value.find((r) => r.bookId === bookId);
    return record ? record.chapterId : null;
  }

  return {
    books,
    chapters,
    selectedBook,
    selectedChapter,
    readingRecords,
    loadBooks,
    loadRecords,
    loadChapters,
    loadChapter,
    continueReading,
    uploadBook,
    getLastReadChapter,
  };
}
