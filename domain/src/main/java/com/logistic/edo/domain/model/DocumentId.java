package com.logistic.edo.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Идентификатор документа (Value Object).
 * Гарантирует, что идентификатор всегда валидный.
 */
public record DocumentId(UUID value) {

    public DocumentId {
        Objects.requireNonNull(value, "DocumentId cannot be null");
    }

    public static DocumentId newId() {
        return new DocumentId(UUID.randomUUID());
    }

    public static DocumentId fromString(String id) {
        return new DocumentId(UUID.fromString(id));
    }
}
