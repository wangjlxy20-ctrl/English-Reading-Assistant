<template>
  <div class="page-wrap">
    <h1>English Reading Assistant</h1>

    <div class="nav">
      <button @click="currentPage = 'bookshelf'">Bookshelf</button>

      <button @click="loadVocabulary()">Vocabulary</button>
    </div>

    <!--BookShelf Page-->
    <div class="books-area" v-if="currentPage === 'bookshelf'">
      <h2>My BookShelf</h2>
      <div v-if="!selectedBook">
        <ul>
          <li 
          v-for="book in books" 
          :key="book.id" 
          @click="loadChapters(book)"
          >
            <div>
              <strong>{{ book.title }}</strong>
            </div>

            <div
              v-if="getLastReadChapter(book.id)"
            >
              Last Read Chapter:
              {{ getLastReadChapter(book.id) }}
            </div>

            <button
              v-if="getLastReadChapter(book.id)"
              @click.stop="continueReading(book)"
            >
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
          <li
            v-for="chapter in chapters"
            :key="chapter.id"
            @click="loadChapter(chapter)"
          >
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
            @click="saveWord(word)"
            class="word"
          >
            {{ word }}
          </span>
        </p>
      </div>
    </div>
    <!--Vocabulary Module-->
    <div v-else-if="currentPage === 'vocabulary'">
      <h2>My Vocabulary</h2>
      <ul>
        <li 
          v-for="item in vocabulary" 
          :key="item.id">
          {{ item.word }}

          <button
            @click="deleteWord(item.id)"
          >
            Delete
          </button>

        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";

const books = ref([]);

const chapters = ref([]);

const selectedBook = ref(null);

const selectedChapter = ref(null);

const words = ref([]);

const currentPage = ref("bookshelf");

const vocabulary = ref([]);

const readingRecords = ref([]);

onMounted(async () => {
  const response = await axios.get("http://localhost:8080/books");

  books.value = response.data;

  const recordResponse = 
    await axios.get(
      "http://localhost:8080/records/1"
    )

    readingRecords.value = 
      recordResponse.data;

});

async function loadChapters(book) {
  selectedBook.value = book;
  const response = await axios.get(
    `http://localhost:8080/chapters/book/${book.id}`,
  );

  chapters.value = response.data;
}

async function loadChapter(chapter) {
  const response = await axios.get(
    `http://localhost:8080/chapters/${chapter.id}`,
  );

  selectedChapter.value = response.data;

  words.value = response.data.content.split(" ");

  await axios.post(
    "http://localhost:8080/records",
    {
      userId:1,
      bookId:selectedBook.value.id,
      chapterId:chapter.id,
      progress:100
    }
  )


}

async function saveWord(word) {
  const response = await axios.post("http://localhost:8080/words", {
    userId: 1,
    chapterId: selectedChapter.value.id,
    word: word,
    meaning: "To Be supplemented",
    example: "",
  });
  if(response.data === "saved"){
    alert(word + " Favorite saved!")
  }else if(response.data === "already exists"){
    alert(word + " Have Collected!")
  }
  
}

async function loadVocabulary() {
  currentPage.value = "vocabulary";

  const response = await axios.get("http://localhost:8080/words/1");

  vocabulary.value = response.data;
}

async function deleteWord(id){
  await axios.delete(
    `http://localhost:8080/words/${id}`
  )

  await loadVocabulary()
}

function getLastReadChapter(bookId){

  const record = 
    readingRecords.value.find(
      r => r.bookId === bookId
    )

    if(record){
      return record.chapterId;
    }

    return null;
}

async function continueReading(book){
  const record = 
    readingRecords.value.find(
      r => r.bookId === book.id
    )

  if(!record){
    alert("No reading record found")
    return
  }

  const response = 
    await axios.get(
      `http://localhost:8080/chapters/${record.chapterId}`
    )

  selectedBook.value = book

  selectedChapter.value = 
    response.data

  words.value = 
    response.data.content.split(" ")
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
</style>
