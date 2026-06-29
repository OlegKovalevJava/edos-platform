package com.logistic.edo.entrypoint.adapter.in.messaging;

import com.logistic.edo.domain.event.DocumentCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DocumentCreatedEventListener {

    @KafkaListener(topics = "document-created", groupId = "edos-consumer-group")
    public void handleDocumentCreated(DocumentCreatedEvent event) {
        log.info("Получено событие из Kafka: documentId={}, companyId={}, status={}",
                event.documentId(), event.companyId(), event.status());
    }
}
