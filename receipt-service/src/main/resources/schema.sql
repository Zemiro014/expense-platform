CREATE TABLE IF NOT EXISTS receipts (
    id UUID PRIMARY KEY,
    merchant VARCHAR(255) NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL
);