create table chunk_embedding(
    id BIGSERIAL primary key,
    chunk_id BIGINT not null,
    content TEXT not null,
    embedding vector(1024),
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

