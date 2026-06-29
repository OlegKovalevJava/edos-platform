package com.logistic.edo.domain.port;

import com.logistic.edo.domain.event.DocumentCreatedEvent;

public interface DocumentEventPublisherPort {
    void publishDocumentCreated(DocumentCreatedEvent event);
}
