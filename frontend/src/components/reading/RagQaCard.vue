<template>
  <div class="ai-card rag-card">
    <div class="ai-card-title">
      <span>🔎 Ask about this book</span>
      <button v-if="ragAnswer" class="icon-btn" @click="clearRagAnswer">✕</button>
    </div>
    <div class="ai-card-body rag-body">
      <div class="rag-input-row">
        <input
          v-model="ragQuestion"
          class="rag-input"
          type="text"
          placeholder="e.g. What happened in Chapter 4?"
          @keyup.enter="askRag"
        />
        <button class="btn-primary small" :disabled="ragLoading || !ragQuestion.trim()"
          @click="askRag">Ask</button>
      </div>

      <div v-if="ragLoading" class="loading-dots"><span></span><span></span><span></span></div>

      <div v-else-if="ragAnswer" class="rag-answer rag-scroll">
        <div class="rag-answer-text">{{ ragAnswer }}</div>

        <div v-if="ragSources.length" class="rag-sources">
          <div class="sa-label">📚 Sources</div>
          <div v-for="src in ragSources" :key="src.id" class="rag-source-item">
            <span class="rag-source-tag">Ch.{{ src.chapterId }} · #{{ src.chunkIndex }}</span>
            <span class="rag-source-snippet">{{ src.content.slice(0, 80) }}...</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRag } from "../../composables/useRag";

const { ragQuestion, ragAnswer, ragSources, ragLoading, askRag, clearRagAnswer } = useRag();
</script>

<style scoped>
.rag-card {
  flex-shrink: 0;
}

.rag-body {
  max-height: none;
  overflow: visible;
}

.rag-scroll {
  max-height: 360px;
  overflow-y: auto;
}

.rag-input-row {
  display: flex;
  gap: 8px;
}

.rag-input {
  flex: 1;
  min-width: 0;
  padding: 8px 10px;
  border: 1px solid #e0ddd6;
  border-radius: 6px;
  font-family: inherit;
  font-size: 0.85rem;
  outline: none;
}

.rag-input:focus {
  border-color: #1a4fa0;
}

.rag-answer {
  margin-top: 14px;
}

.rag-answer-text {
  font-size: 0.88rem;
  line-height: 1.7;
  color: #2c2c2c;
  white-space: pre-wrap;
}

.rag-sources {
  margin-top: 14px;
  border-top: 1px solid #eee;
  padding-top: 10px;
}

.rag-source-item {
  display: flex;
  flex-direction: column;
  gap: 2px;
  margin-bottom: 8px;
  font-size: 0.8rem;
}

.rag-source-tag {
  font-weight: bold;
  color: #1a4fa0;
}

.rag-source-snippet {
  color: #777;
  overflow-wrap: break-word;
}

/* sa-label reused here for the "Sources" heading — same look as
   SentenceAnalysisCard's section labels, redeclared locally since
   scoped styles don't leak across components. */
.sa-label {
  font-weight: bold;
  font-size: 0.78rem;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  color: #888;
  margin-bottom: 4px;
}
</style>
