<template>
  <div class="ai-card">
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
</template>

<script setup>
import { useWordPanel } from "../../composables/useWordPanel";

const { selectedAiWord, closeAiPanel, aiLoading, aiExplanation, parseAiText } = useWordPanel();
</script>

<style scoped>
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
</style>
