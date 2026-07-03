<template>
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
</template>

<script setup>
import { useVocabulary } from "../composables/useVocabulary";

const {
  vocabulary,
  selectedWord,
  dictionaryMeaning,
  dictionaryExample,
  deleteWord,
  showWordDetail,
} = useVocabulary();
</script>

<style scoped>
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
</style>
