import { ref } from "vue";
import request from "../api/request";
import { useBooks } from "./useBooks";
import { useWordPanel } from "./useWordPanel";

const showSentencePanel = ref(false);
const sentenceLoading = ref(false);
const sentenceAnalysis = ref(null);
const currentSentence = ref("");
const selectedSentence = ref("");
let lastSelectedText = "";

export function useSentencePanel() {
  const { selectedBook, selectedChapter } = useBooks();
  const { showMenu } = useWordPanel();

  function isSentenceHighlighted(sentence) {
    if (!selectedSentence.value) return false;
    const norm = (s) => s.replace(/\s+/g, " ").trim().toLowerCase();
    return (
      norm(sentence).includes(norm(selectedSentence.value)) ||
      norm(selectedSentence.value).includes(norm(sentence))
    );
  }

  async function analyzeSentence(sentence) {
    currentSentence.value = sentence;
    showSentencePanel.value = true;
    sentenceLoading.value = true;
    try {
      const res = await request.post("/sentence/analyze", {
        userId: 1,
        bookId: selectedBook.value.id,
        chapterId: selectedChapter.value.id,
        sentence,
      });
      sentenceAnalysis.value = res.data;
    } catch (e) {
      console.error(e);
    } finally {
      sentenceLoading.value = false;
    }
  }

  function closeSentencePanel() {
    showSentencePanel.value = false;
    sentenceAnalysis.value = null;
    selectedSentence.value = "";
    lastSelectedText = "";
  }

  // FIX #3: Selection handler
  async function handleSelection() {
    const selection = window.getSelection();
    const text = selection?.toString().trim();
    if (!text || text === lastSelectedText) return;

    // word count: split on whitespace — works correctly because browser gives
    // natural text even though we render tokens without &nbsp;
    const wordCount = text.split(/\s+/).filter((w) => /[A-Za-z]/.test(w)).length;

    if (wordCount < 3) {
      // single-word click — let the word click handler manage it
      return;
    }

    selectedSentence.value = text;
    lastSelectedText = text;
    showMenu.value = false;

    // Keep highlight visible while loading
    await analyzeSentence(text);

    // Clear browser selection after analysis is triggered
    selection.removeAllRanges();
  }

  return {
    showSentencePanel,
    sentenceLoading,
    sentenceAnalysis,
    currentSentence,
    selectedSentence,
    isSentenceHighlighted,
    analyzeSentence,
    closeSentencePanel,
    handleSelection,
  };
}
