CREATE TABLE account (
    id BIGSERIAL PRIMARY KEY,
    account_number VARCHAR(255) UNIQUE NOT NULL,
    deleted BOOLEAN NOT NULL,
    currency CHAR(3) NOT NULL,
    total_credit NUMERIC(15,2) NOT NULL,
    available_credit NUMERIC(15,2) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    client_id BIGINT REFERENCES client (id)
);
