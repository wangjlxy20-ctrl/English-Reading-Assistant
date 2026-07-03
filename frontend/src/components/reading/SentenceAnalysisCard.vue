<template>
  <div class="ai-card">
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
</template>

<script setup>
import { useSentencePanel } from "../../composables/useSentencePanel";

const { sentenceLoading, sentenceAnalysis, closeSentencePanel } = useSentencePanel();
</script>

<style scoped>
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
</style>
