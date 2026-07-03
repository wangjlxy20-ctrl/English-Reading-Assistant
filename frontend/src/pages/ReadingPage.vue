<template>
  <div class="reading-layout">
    <ArticleView />
    <div class="reading-right">
      <AiSidebar />
    </div>
  </div>
</template>

<script setup>
import ArticleView from "../components/reading/ArticleView.vue";
import AiSidebar from "../components/reading/AiSidebar.vue";
</script>

<style scoped>
/* ─────────────────────────────────────────────────────────────────────────── */
/* ── Reading Layout: LEFT / RIGHT split ─────────────────────────────────── */
/* ─────────────────────────────────────────────────────────────────────────── */
.reading-layout {
  display: flex;
  gap: 0;
  height: calc(100vh - 80px);
  /* subtract topbar */
  overflow: hidden;
}

/* ── Right sidebar ─────────────────────────────────────────────────────────── */
.reading-right {
  flex: 0 0 38%;
  max-width: 420px;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}
</style>
