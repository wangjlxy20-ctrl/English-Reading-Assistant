create table sentence_embedding(
    id BIGSERIAL primary key,
    sentence_id BIGINT not null,
    content TEXT not null,
    embedding vector(1024),
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX sentence_embedding_vector_idx
on sentence_embedding
using ivfflat (embedding vector_cosine_ops);