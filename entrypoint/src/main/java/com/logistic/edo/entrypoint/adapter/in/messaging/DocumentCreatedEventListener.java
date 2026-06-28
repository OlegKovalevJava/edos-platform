package com.logistic.edo.entrypoint.adapter.in.messaging;

import com.logistic.edo.domain.event.DocumentCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DocumentCreatedEventListener {

    private static final Logger log = LoggerFactory.getLogger(DocumentCreatedEventListener.class);

    @KafkaListener(topics = "document-created", groupId = "edos-consumer-group")
    public void handleDocumentCreated(DocumentCreatedEvent event) {
        log.info("✅ Получено событие из Kafka: documentId={}, companyId={}, status={}",
                event.documentId(), event.companyId(), event.status());
    }
}
