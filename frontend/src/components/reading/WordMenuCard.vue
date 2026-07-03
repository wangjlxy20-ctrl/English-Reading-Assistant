<template>
  <div class="ai-card word-menu-card">
    <div class="ai-card-title">
      <span>🔤 {{ currentWord }}</span>
      <button class="icon-btn" @click="showMenu = false">✕</button>
    </div>
    <div class="menu-btns">
      <button class="btn-primary" @click="saveCurrentWord">⭐ Collect</button>
      <button class="btn-primary" @click="explainCurrentWord">🤖 AI Explain</button>
    </div>
  </div>
</template>

<script setup>
import { useWordPanel } from "../../composables/useWordPanel";

const { currentWord, showMenu, saveCurrentWord, explainCurrentWord } = useWordPanel();
</script>

<style scoped>
.word-menu-card .ai-card-body {
  display: none;
}

.menu-btns {
  display: flex;
  gap: 10px;
  padding: 14px 16px;
}
</style>
