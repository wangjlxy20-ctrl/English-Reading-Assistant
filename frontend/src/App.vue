<template>
  <div class="page-wrap">
    <h1>English Reading Assistant</h1>

    <div class="nav">
      <button @click="currentPage = 'bookshelf'">Bookshelf</button>
      <input type="file" @change="uploadBook" />
      <button @click="loadVocabulary()">Vocabulary</button>
    </div>

    <!--BookShelf Page-->
    <div class="books-area" v-if="currentPage === 'bookshelf'">
      <h2>My BookShelf</h2>

      <div v-if="!selectedBook">
        <ul>
          <li v-for="book in books" :key="book.id" @click="loadChapters(book)">
            <div>
              <strong>{{ book.title }}</strong>
            </div>

            <div v-if="getLastReadChapter(book.id)">
              Last Read Chapter:
              {{ getLastReadChapter(book.id) }}
            </div>

            <button v-if="getLastReadChapter(book.id)" @click.stop="continueReading(book)">
              Continue Reading
            </button>

          </li>
        </ul>
      </div>

      <!-- Chapter List Page -->
      <div v-else-if="!selectedChapter">
        <button @click="selectedBook = null">Return to Bookshelf</button>

        <h2>{{ selectedBook.title }}</h2>

        <ul>
          <li v-for="chapter in chapters" :key="chapter.id" @click="loadChapter(chapter)">
            {{ chapter.title }}
          </li>
        </ul>
      </div>

      <!-- Reading Page -->
      <div v-else>
        <button @click="selectedChapter = null">Return to Chapters</button>
        <h2>{{ selectedChapter.title }}</h2>
        <p class="content">
          <span 
          v-for="word in words" 
          :key="word" 
          @click="openWordMenu(word)" 
          class="word">
            {{ word }}
          </span>
        </p>

        <div
        v-if="showMenu"
        class="word-menu"
        >

          <h3>
            {{ currentWord }}
          </h3>

          <button @click="saveCurrentWord">
            ⭐ Collect
          </button>

          <button @click="explainCurrentWord">
            🤖 AI Explain
          </button>

          <button @click="showMenu = false">
            ✖ Close
          </button>

        </div>
        
        <div
        v-if="showAiPanel"
        class="ai-panel"
        >

          <button
            class="close-btn"
            @click="closeAiPanel"
          >
            ✖ Close
          </button>


          <h3>
            AI Explanation:
            {{ selectedAiWord }}
          </h3>

          <pre>{{ aiExplanation }}</pre>

        </div>



      </div>


    </div>


    <!--Vocabulary Module-->
    <div v-else-if="currentPage === 'vocabulary'">
      <h2>My Vocabulary</h2>
      <ul>
        <li v-for="item in vocabulary" :key="item.id" @click="showWordDetail(item)">
          {{ item.word }}

          <button @click.stop="deleteWord(item.id)">
            Delete
          </button>

        </li>
      </ul>


      <div v-if="selectedWord" class="word-detail">
        <h3>Word Detail</h3>

        <p>
          <strong>Word:</strong>
          {{ selectedWord.word }}
        </p>

        <p>
          <strong>Meaning:</strong>
          {{ dictionaryMeaning }}
        </p>

        <p>
          <strong>Example:</strong>
          {{ dictionaryExample }}
        </p>


      </div>

    </div>


  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import request from "./api/request";

const books = ref([]);

const chapters = ref([]);

const selectedBook = ref(null);

const selectedChapter = ref(null);

const words = ref([]);

const currentPage = ref("bookshelf");

const vocabulary = ref([]);

const selectedWord = ref(null);

const dictionaryMeaning = ref("");

const dictionaryExample = ref("");

const aiExplanation = ref("");

const selectedAiWord = ref("");

const readingRecords = ref([]);

const currentWord = ref("");

const showMenu = ref(false);

const showAiPanel = ref(false);

onMounted(async () => {
  await Promise.all([
    loadBooks(),
    loadRecords()
  ])

});


async function loadChapters(book) {
  selectedBook.value = book;
  const response = await request.get(
    `/chapters/book/${book.id}`,
  );

  chapters.value = response.data;
}

async function loadChapter(chapter) {
  const response = await request.get(
    `/chapters/${chapter.id}`,
  );

  selectedChapter.value = response.data;

  words.value = tokenize(response.data.content);

  await request.post(
    "/records",
    {
      userId: 1,
      bookId: selectedBook.value.id,
      chapterId: chapter.id,
      progress: 100
    }
  )


}

async function saveWord(word) {
  const response = await request.post("/words", {
    userId: 1,
    chapterId: selectedChapter.value.id,
    word: word,
    meaning: "To Be supplemented",
    example: "",
  });
  if (response.data === "saved") {
    alert(word + " Favorite saved!")
  } else if (response.data === "already exists") {
    alert(word + " Have Collected!")
  }

}

async function loadVocabulary() {
  currentPage.value = "vocabulary";

  const response = await request.get("/words/1");

  vocabulary.value = response.data;
}

async function deleteWord(id) {
  await request.delete(
    `/words/${id}`
  )

  await loadVocabulary()
}

function getLastReadChapter(bookId) {

  const record =
    readingRecords.value.find(
      r => r.bookId === bookId
    )

  if (record) {
    return record.chapterId;
  }

  return null;
}

async function continueReading(book) {
  const record =
    readingRecords.value.find(
      r => r.bookId === book.id
    )

  if (!record) {
    alert("No reading record found")
    return
  }

  const response =
    await request.get(
      `/chapters/${record.chapterId}`
    )

  selectedBook.value = book

  selectedChapter.value =
    response.data

  words.value = 
  tokenize(response.data.content);
}


async function lookupWord(word, item) {

  try {

    const response =
      await axios.get(
        `https://api.dictionaryapi.dev/api/v2/entries/en/${word}`
      )


    const meanings =
      response.data[0].meanings;


    dictionaryMeaning.value =
      meanings[0]
        .definitions[0]
        .definition



    let example = null;


    for (const meaning of meanings) {

      for (const def of meaning.definitions) {

        if (def.example) {
          example = def.example
          break;
        }

      }

      if (example) break;
    }


    dictionaryExample.value =
      example || "No example available"



    await request.put(
      `/words/${item.id}`,
      {
        meaning: dictionaryMeaning.value,
        example: dictionaryExample.value
      }
    )


    item.meaning =
      dictionaryMeaning.value;


    item.example =
      dictionaryExample.value;


  } catch (error) {

    console.log(error)

    dictionaryMeaning.value =
      "Definition not found"


    dictionaryExample.value =
      "No example available"

  }

}


async function showWordDetail(item) {

  selectedWord.value = item

  if (
    item.meaning &&
    item.meaning !== "To Be supplemented"
  ) {

    console.log("Load catch")

    dictionaryMeaning.value =
      item.meaning

    dictionaryExample.value =
      item.example

    return

  }

  console.log("Call Dictionary API")
  await lookupWord(item.word, item)

}


async function explainWord(word) {
  
  try{
    selectedAiWord.value = word

    const response = 
      await request.post(
        "/ai/word",
          {
            word:word
          }
      )

      aiExplanation.value = 
          response.data
  } catch(error){

    console.log(error)

    aiExplanation.value = 
      "AI analysis failed"
  }
}


function openWordMenu(word){
  currentWord.value = word;
  showMenu.value = true;
}


async function saveCurrentWord(){

  await saveWord(
    currentWord.value
  );

}


async function explainCurrentWord(){

  await explainWord(
    currentWord.value
  );

  showAiPanel.value = true;

}


function closeAiPanel(){

  showAiPanel.value = false;

  aiExplanation.value = "";

  selectedAiWord.value = "";

}


async function uploadBook(event) {

  const file = event.target.files[0]

  if(!file){
    console.log("No file selected");
    return;
  }


  const formData = new FormData()

  formData.append("file",file)

  try{
    const res = await request.post(
    "/books/upload",
    formData,
    {
      headers:{
        "Content-Type":"multipart/form-data"
      }
    }
  )

    console.log("upload successful!",res.data)


  await loadBooks();


  }catch(err){
    console.error("upload failed",err);
  }

}


async function loadBooks() {
  const res = await request.get("/books/list")
  books.value = res.data;
}

async function loadRecords() {
  const res = await request.get("/records/1")
  readingRecords.value = res.data;
  
}

//unified chunking function
function tokenize(text) {
  return text
    .replace(/[\n\r]/g," ")
    .replace(/[^a-zA-Z0-9'’-]+/g," ")
    .trim()
    .split(/\s+/)
}
</script>

<style scoped>
.page-wrap h1 {
  text-align: center;
}

.books-area {
  text-align: left;
  padding-left: 40px;
}

ul {
  padding-left: 20px;
}

.word {
  margin-right: 6px;
  cursor: pointer;
}

.word-detail {
  margin-top: 20px;
  padding: 15px;
  border: 1px solid #ccc;
  border-radius: 8px;
}

.ai-panel {

  margin-top: 20px;

  padding: 15px;

  border: 1px solid #ccc;

  border-radius: 8px;

  max-width: 800px;

}

.ai-btn {

  margin-left: 4px;

  cursor: pointer;

}

.save-btn {

  margin-left: 4px;

  cursor: pointer;

}
</style>
