package com.logistic.edo.domain.event;

import com.logistic.edo.domain.model.DocumentId;
import com.logistic.edo.domain.model.DocumentStatus;

import java.time.Instant;

public record DocumentCreatedEvent(
        DocumentId documentId,
        String companyId,
        DocumentStatus status,
        Instant createdAt
) {
}
