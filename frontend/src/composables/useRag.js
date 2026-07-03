import { ref } from "vue";
import request from "../api/request";

const ragQuestion = ref("");
const ragAnswer = ref("");
const ragSources = ref([]);
const ragLoading = ref(false);

export function useRag() {
  function clearRagAnswer() {
    ragAnswer.value = "";
    ragSources.value = [];
    ragQuestion.value = "";
  }

  async function askRag() {
    const question = ragQuestion.value.trim();
    if (!question) return;

    ragLoading.value = true;
    ragAnswer.value = "";
    ragSources.value = [];

    try {
      const res = await request.get("/rag/ask", { params: { question } });
      ragAnswer.value = res.data.answer;
      ragSources.value = res.data.sources || [];
    } catch (e) {
      console.error(e);
      ragAnswer.value = "Failed to get an answer, please try again.";
    } finally {
      ragLoading.value = false;
    }
  }

  return { ragQuestion, ragAnswer, ragSources, ragLoading, askRag, clearRagAnswer };
}
