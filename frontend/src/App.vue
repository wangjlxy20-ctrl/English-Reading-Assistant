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
        <button class="btn-ghost" @click="goToVocabulary">Vocabulary</button>
      </div>
    </header>

    <div class="main-content">
      <BookshelfPage v-if="currentPage === 'bookshelf'" />
      <VocabularyPage v-else-if="currentPage === 'vocabulary'" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import "./assets/shared.css";

import BookshelfPage from "./pages/BookshelfPage.vue";
import VocabularyPage from "./pages/VocabularyPage.vue";

import { useBooks } from "./composables/useBooks";
import { useVocabulary } from "./composables/useVocabulary";

const currentPage = ref("bookshelf");

const { loadBooks, loadRecords, uploadBook } = useBooks();
const { loadVocabulary } = useVocabulary();

function goToVocabulary() {
  currentPage.value = "vocabulary";
  loadVocabulary();
}

onMounted(async () => {
  await Promise.all([loadBooks(), loadRecords()]);
});
</script>

<style scoped>
/* App.vue only keeps the shell — page/feature-specific styles now
   live inside their own components. Shared primitives (buttons,
   ai-card, loading-dots, transitions, reset) live in
   ./assets/shared.css so every child component can use them too,
   since Vue's `scoped` styles don't cross component boundaries. */
</style>
