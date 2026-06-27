CREATE SCHEMA IF NOT EXISTS edo;

CREATE TABLE IF NOT EXISTS edo.documents (
    id UUID PRIMARY KEY,
    status VARCHAR(20) NOT NULL,
    xml_content TEXT NOT NULL,
    company_id VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_documents_company_id ON edo.documents (company_id);