<template>
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
</template>

<script setup>
import { useBooks } from "../../composables/useBooks";
import { useReading } from "../../composables/useReading";
import { useWordPanel } from "../../composables/useWordPanel";
import { useSentencePanel } from "../../composables/useSentencePanel";

const { selectedChapter } = useBooks();
const { sentences, tokenize } = useReading();
const { openWordMenu } = useWordPanel();
const { isSentenceHighlighted, handleSelection } = useSentencePanel();
</script>

<style scoped>
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
</style>
