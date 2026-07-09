# English Reading Assistant

English Original Reading Assistant Based on ：Spring Boot + MyBatis + MySQL + Vue3 + Qwen-turbo + PostgreSQL + RAG + Redis

## Tech Stack

Backend:

- Spring Boot
- MyBatis
- Maven

Frontend:

- Vue3
- Axios

AI:

- Qwen-turbo

DATABASE:

- PostgreSQL
- MySQL
- Redis

## Features

- Book management
- Chapter reading
- Reading progress tracking
- Vue3 frontend
- Vocabulary management
- Duplicate Prevention
- Continue Reading
- Vocabulary Cache
- AI reading assistant
- Retrieval-Augmented Generation ehances AI searching ability
- Apply database schema migration via Flyway SQL scripts

## Current API

GET /books

GET /chapters/book/{bookId}

GET /records/{userId}

POST /records

FreeDictionaryAPI

Qwen-turbo API

Qwen-text-embedding-v4

## Future

- Optimize the splitting algorithm for English text
- Use a filter to screen out common article,conjunctions and other words that do not affect reading comprehension.
- Import PDF/EPUB Files
- Implement user login and registration functions

## Project Presentation :

![alt text](./docs/RAG_Ability_Show.png)

![alt text](./docs/New_fronted_BookShelf.png)

![alt text](./docs/New_frontend_ReadingPage.png)

![alt text](./docs/PostgreSQL_Embedding_Vector_Show.png)

![alt text](./docs/Embeddinig_OverLap_Show.png)

![alt text](./docs/PG_Chunk_table_show.png)

![alt text](./docs/Redis_Spring_terminal_show.png)

![alt text](./docs/RAG_Redis_Cache_Show.png)

![alt text](./docs/redis_docker_show.png)

![alt text](./docs/Chapter_redis_cache_show.png)

![alt text](./docs/ReadingPageShow.png)

![alt text](./docs/Load_Catche.png)

![alt text](./docs/AI_Chat_Show.png)

![alt text](./docs/MySQL_Chunk_Show.png)

![alt text](./docs/PostgreSQL_Show.png)

## Author

WangJiaLe<br>
Computer Science Student<br>
Java Backend Development Leaner
