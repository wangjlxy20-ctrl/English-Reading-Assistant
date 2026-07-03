<template>
  <div class="ai-sidebar">
    <transition name="fade">
      <WordMenuCard v-if="showMenu" />
    </transition>

    <transition name="fade">
      <WordExplanationCard v-if="showAiPanel" />
    </transition>

    <transition name="fade">
      <SentenceAnalysisCard v-if="showSentencePanel" />
    </transition>

    <!-- Empty state -->
    <div v-if="!showMenu && !showAiPanel && !showSentencePanel" class="ai-empty">
      <div class="ai-empty-icon">💬</div>
      <p>Click a word to look it up,<br>or drag to select a sentence for analysis.</p>
    </div>

    <RagQaCard />
  </div>
</template>

<script setup>
import WordMenuCard from "./WordMenuCard.vue";
import WordExplanationCard from "./WordExplanationCard.vue";
import SentenceAnalysisCard from "./SentenceAnalysisCard.vue";
import RagQaCard from "./RagQaCard.vue";

import { useWordPanel } from "../../composables/useWordPanel";
import { useSentencePanel } from "../../composables/useSentencePanel";

const { showMenu, showAiPanel } = useWordPanel();
const { showSentencePanel } = useSentencePanel();
</script>

<style scoped>
.ai-sidebar {
  flex: 1;
  overflow-y: auto;
  padding: 20px 20px 20px 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
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
</style>
