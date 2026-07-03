import { ref } from "vue";

const sentences = ref([]);
const words = ref([]);

export function useReading() {
  // FIX #1: tokenize returns typed tokens, not flat strings.
  // This preserves word-count accuracy: when user selects text in the
  // browser, the native Selection API returns text with single spaces
  // between words, so split(/\s+/) counts correctly — even though we
  // no longer inject &nbsp; tokens.
  function tokenize(text) {
    const raw = text.match(/[A-Za-z]+(?:'[A-Za-z]+)?|[.,!?;:()"'\-]/g) || [];
    const result = [];
    for (let i = 0; i < raw.length; i++) {
      const val = raw[i];
      const isWord = /[A-Za-z]/.test(val);
      result.push({ type: isWord ? "word" : "punct", value: val });
      // Add a space after every token except: before punctuation, or at the end
      const next = raw[i + 1];
      if (next && /[.,!?;:)"'\-]/.test(next)) continue; // no space before punct
      if (i < raw.length - 1) result.push({ type: "space", value: " " });
    }
    return result;
  }

  function splitSentences(text) {
    return text
      .replace(/\r\n/g, "\n")
      .split(/\n\s*\n|(?<=[.!?])\s+/)
      .map((s) => s.trim())
      .filter((s) => s.length > 0);
  }

  return { sentences, words, tokenize, splitSentences };
}
