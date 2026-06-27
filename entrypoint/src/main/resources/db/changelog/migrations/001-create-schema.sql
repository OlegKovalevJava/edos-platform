CREATE TABLE IF NOT EXISTS documents (
    id UUID PRIMARY KEY,
    status VARCHAR(20) NOT NULL,
    xml_content TEXT NOT NULL,
    company_id VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_documents_company_id ON documents (company_id);