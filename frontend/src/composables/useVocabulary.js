import { ref } from "vue";
import axios from "axios";
import request from "../api/request";

const vocabulary = ref([]);
const selectedWord = ref(null);
const dictionaryMeaning = ref("");
const dictionaryExample = ref("");

export function useVocabulary() {
  async function loadVocabulary() {
    const response = await request.get("/words/1");
    vocabulary.value = response.data;
  }

  async function deleteWord(id) {
    await request.delete(`/words/${id}`);
    await loadVocabulary();
  }

  async function lookupWord(word, item) {
    try {
      const response = await axios.get(
        `https://api.dictionaryapi.dev/api/v2/entries/en/${word}`
      );
      const meanings = response.data[0].meanings;
      dictionaryMeaning.value = meanings[0].definitions[0].definition;
      let example = null;
      for (const m of meanings) {
        for (const d of m.definitions) {
          if (d.example) {
            example = d.example;
            break;
          }
        }
        if (example) break;
      }
      dictionaryExample.value = example || "No example available";
      await request.put(`/words/${item.id}`, {
        meaning: dictionaryMeaning.value,
        example: dictionaryExample.value,
      });
      item.meaning = dictionaryMeaning.value;
      item.example = dictionaryExample.value;
    } catch {
      dictionaryMeaning.value = "Definition not found";
      dictionaryExample.value = "No example available";
    }
  }

  async function showWordDetail(item) {
    selectedWord.value = item;
    if (item.meaning && item.meaning !== "To Be supplemented") {
      dictionaryMeaning.value = item.meaning;
      dictionaryExample.value = item.example;
      return;
    }
    await lookupWord(item.word, item);
  }

  return {
    vocabulary,
    selectedWord,
    dictionaryMeaning,
    dictionaryExample,
    loadVocabulary,
    deleteWord,
    showWordDetail,
  };
}
