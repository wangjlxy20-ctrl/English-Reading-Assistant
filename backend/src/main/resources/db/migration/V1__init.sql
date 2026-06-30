------------------------------------------------------
-- ERA USER
------------------------------------------------------

CREATE TABLE era_user (
                          id BIGSERIAL PRIMARY KEY,

                          username VARCHAR(50) UNIQUE NOT NULL,

                          password VARCHAR(255) NOT NULL,

                          email VARCHAR(100),

                          create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

------------------------------------------------------
-- BOOK
------------------------------------------------------

CREATE TABLE book (
                      id BIGSERIAL PRIMARY KEY,

                      title VARCHAR(100) NOT NULL,

                      author VARCHAR(100),

                      cover_url VARCHAR(255),

                      description TEXT,

                      total_chapters INT NOT NULL DEFAULT 0,

                      create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

------------------------------------------------------
-- CHAPTER
------------------------------------------------------

CREATE TABLE chapter (
                         id BIGSERIAL PRIMARY KEY,

                         book_id BIGINT NOT NULL,

                         chapter_no INT NOT NULL,

                         title VARCHAR(100),

                         content TEXT,

                         CONSTRAINT fk_chapter_book
                             FOREIGN KEY (book_id)
                                 REFERENCES book(id)
                                 ON DELETE CASCADE
);

CREATE INDEX idx_chapter_book
    ON chapter(book_id);

------------------------------------------------------
-- READING RECORD
------------------------------------------------------

CREATE TABLE reading_record (
                                id BIGSERIAL PRIMARY KEY,

                                user_id BIGINT NOT NULL,

                                book_id BIGINT NOT NULL,

                                chapter_id BIGINT NOT NULL,

                                progress INT DEFAULT 0,

                                last_read_time TIMESTAMP,

                                CONSTRAINT fk_record_user
                                    FOREIGN KEY(user_id)
                                        REFERENCES era_user(id)
                                        ON DELETE CASCADE,

                                CONSTRAINT fk_record_book
                                    FOREIGN KEY(book_id)
                                        REFERENCES book(id)
                                        ON DELETE CASCADE,

                                CONSTRAINT fk_record_chapter
                                    FOREIGN KEY(chapter_id)
                                        REFERENCES chapter(id)
                                        ON DELETE CASCADE,

                                CONSTRAINT uk_user_book_chapter
                                    UNIQUE(user_id, book_id, chapter_id)
);

------------------------------------------------------
-- WORD
------------------------------------------------------

CREATE TABLE word (
                      id BIGSERIAL PRIMARY KEY,

                      user_id BIGINT NOT NULL,

                      chapter_id BIGINT NOT NULL,

                      word VARCHAR(100) NOT NULL,

                      meaning VARCHAR(255),

                      example TEXT,

                      create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                      CONSTRAINT fk_word_user
                          FOREIGN KEY(user_id)
                              REFERENCES era_user(id)
                              ON DELETE CASCADE,

                      CONSTRAINT fk_word_chapter
                          FOREIGN KEY(chapter_id)
                              REFERENCES chapter(id)
                              ON DELETE CASCADE
);

------------------------------------------------------
-- AI CHAT
------------------------------------------------------

CREATE TABLE ai_chat (
                         id BIGSERIAL PRIMARY KEY,

                         user_id BIGINT NOT NULL,

                         question TEXT,

                         answer TEXT,

                         context TEXT,

                         create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                         CONSTRAINT fk_chat_user
                             FOREIGN KEY(user_id)
                                 REFERENCES era_user(id)
                                 ON DELETE CASCADE
);

------------------------------------------------------
-- SENTENCE ANALYSIS
------------------------------------------------------

CREATE TABLE sentence_analysis (
                                   id BIGSERIAL PRIMARY KEY,

                                   book_id BIGINT NOT NULL,

                                   chapter_id BIGINT NOT NULL,

                                   sentence TEXT,

                                   meaning TEXT,

                                   grammar JSONB,

                                   key_phrases JSONB,

                                   difficulty INT,

                                   create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                   CONSTRAINT fk_analysis_book
                                       FOREIGN KEY(book_id)
                                           REFERENCES book(id)
                                           ON DELETE CASCADE,

                                   CONSTRAINT fk_analysis_chapter
                                       FOREIGN KEY(chapter_id)
                                           REFERENCES chapter(id)
                                           ON DELETE CASCADE
);

------------------------------------------------------
-- CHUNK
------------------------------------------------------

CREATE TABLE chunk (
                       id BIGSERIAL PRIMARY KEY,

                       book_id BIGINT NOT NULL,

                       chapter_id BIGINT NOT NULL,

                       chunk_index INT NOT NULL,

                       content TEXT NOT NULL,

                       token_count INT NOT NULL,

                       embedding_status SMALLINT DEFAULT 0,

                       create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                       CONSTRAINT fk_chunk_book
                           FOREIGN KEY(book_id)
                               REFERENCES book(id)
                               ON DELETE CASCADE,

                       CONSTRAINT fk_chunk_chapter
                           FOREIGN KEY(chapter_id)
                               REFERENCES chapter(id)
                               ON DELETE CASCADE
);

CREATE INDEX idx_chunk_book
    ON chunk(book_id);

CREATE INDEX idx_chunk_chapter
    ON chunk(chapter_id);

CREATE INDEX idx_sentence_analysis_chapter
    ON sentence_analysis(chapter_id);

CREATE INDEX idx_word_user
    ON word(user_id);

CREATE INDEX idx_record_user
    ON reading_record(user_id);