package com.logistic.edo.entrypoint.adapter.out.messaging;

import com.logistic.edo.domain.event.DocumentCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DocumentEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topic.document-created:document-created}")
    private String topic;

    public void publishDocumentCreated(DocumentCreatedEvent event) {
        log.info("Publishing DocumentCreatedEvent for document: {}", event.documentId());
        kafkaTemplate.send(topic, event.documentId().value().toString(), event);
    }
}