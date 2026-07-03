import { ref } from "vue";
import request from "../api/request";
import { useBooks } from "./useBooks";

const currentWord = ref("");
const showMenu = ref(false);
const showAiPanel = ref(false);
const aiLoading = ref(false);
const aiExplanation = ref("");
const selectedAiWord = ref("");

export function useWordPanel() {
  const { selectedChapter } = useBooks();

  function openWordMenu(word) {
    if (!word) return;
    currentWord.value = word;
    showMenu.value = true;
    // Close other panels when opening word menu
    showAiPanel.value = false;
  }

  async function saveWord(word) {
    const response = await request.post("/words", {
      userId: 1,
      chapterId: selectedChapter.value.id,
      word: word,
      meaning: "To Be supplemented",
      example: "",
    });
    if (response.data === "saved") alert(word + " saved!");
    else if (response.data === "already exists") alert(word + " already collected!");
  }

  async function saveCurrentWord() {
    await saveWord(currentWord.value);
  }

  async function explainWord(word) {
    try {
      selectedAiWord.value = word;
      const response = await request.post("/ai/word", { word });
      aiExplanation.value = response.data;
    } catch {
      aiExplanation.value = "AI analysis failed";
    }
  }

  async function explainCurrentWord() {
    showMenu.value = false;
    showAiPanel.value = true;
    aiLoading.value = true;
    await explainWord(currentWord.value);
    aiLoading.value = false;
  }

  function closeAiPanel() {
    showAiPanel.value = false;
    aiExplanation.value = "";
    selectedAiWord.value = "";
  }

  // Parse AI formatted text for display
  function parseAiText(text) {
    if (!text) return [];
    return text
      .split("\n")
      .filter((l) => l.trim())
      .map((line) => ({
        type: line.startsWith("【") ? "heading" : "paragraph",
        text: line,
      }));
  }

  return {
    currentWord,
    showMenu,
    showAiPanel,
    aiLoading,
    aiExplanation,
    selectedAiWord,
    openWordMenu,
    saveCurrentWord,
    explainCurrentWord,
    closeAiPanel,
    parseAiText,
  };
}
