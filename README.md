# English Reading Assistant

English Original Reading Assistant Based on ：Spring Boot + MyBatis + MySQL + Vue3 + Qwen-turbo

## Tech Stack

Backend:

- Spring Boot
- MyBatis
- MySQL
- Maven

Frontend:

- Vue3
- Axios

AI:

- Qwen-turbo

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

## Current API

GET /books

GET /chapters/book/{bookId}

GET /records/{userId}

POST /records

FreeDictionaryAPI

Qwen-turbo API

## Future

- Optimize the splitting algorithm for English text
- Use a filter to screen out common article,conjunctions and other words that do not affect reading comprehension.
- Import PDF/EPUB Files
- Implement user login and registration functions

## Project Presentation

![alt text](./docs/ReadingPageShow.png)

![alt text](./docs/Load_Catche.png)

![alt text](./docs/AI_Chat_Show.png)

## Author

WangJiaLe<br>
Computer Science Student<br>
Java Backend Development Leaner
