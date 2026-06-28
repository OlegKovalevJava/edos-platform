package com.logistic.edo.domain.port;

import com.logistic.edo.domain.model.Document;
import com.logistic.edo.domain.model.DocumentId;

import java.util.Optional;

public interface DocumentRepositoryPort {
    Document save(Document document);
    Optional<Document> findById(DocumentId id);
}