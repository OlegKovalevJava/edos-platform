package com.logistic.edo.usecases.port.out;

import com.logistic.edo.domain.event.DocumentCreatedEvent;

public interface DocumentEventPublisherPort {
    void publishDocumentCreated(DocumentCreatedEvent event);
}
