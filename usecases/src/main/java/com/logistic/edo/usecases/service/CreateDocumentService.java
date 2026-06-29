package com.logistic.edo.usecases.service;

import com.logistic.edo.domain.event.DocumentCreatedEvent;
import com.logistic.edo.domain.model.Document;
import com.logistic.edo.domain.model.DocumentId;
import com.logistic.edo.domain.port.DocumentRepositoryPort;
import com.logistic.edo.domain.port.DocumentEventPublisherPort;
import com.logistic.edo.domain.port.XmlTransformationPort;
import com.logistic.edo.usecases.command.CreateDocumentCommand;
import com.logistic.edo.usecases.port.in.CreateDocumentUseCase;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreateDocumentService implements CreateDocumentUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateDocumentService.class);

    private final DocumentRepositoryPort documentRepository;
    private final DocumentEventPublisherPort eventPublisher;
    private final XmlTransformationPort xmlTransformationPort;
    private final Counter documentCreatedCounter;

    public CreateDocumentService(DocumentRepositoryPort documentRepository,
                                 DocumentEventPublisherPort eventPublisher,
                                 XmlTransformationPort xmlTransformationPort,
                                 MeterRegistry meterRegistry) {
        this.documentRepository = documentRepository;
        this.eventPublisher = eventPublisher;
        this.xmlTransformationPort = xmlTransformationPort;
        this.documentCreatedCounter = Counter.builder("documents.created")
                .description("Total number of created documents")
                .register(meterRegistry);
        log.info("CreateDocumentService initialized with XML Transformation Port and Metrics");
    }

    @Override
    public DocumentId execute(CreateDocumentCommand command) {
        log.info("Executing CreateDocumentService with command: companyId={}", command.companyId());

        // 1. Валидация
        if (command.xmlContent() == null || command.xmlContent().isBlank()) {
            throw new IllegalArgumentException("Содержимое документа не может быть пустым");
        }
        if (command.companyId() == null || command.companyId().isBlank()) {
            throw new IllegalArgumentException("Идентификатор компании не может быть пустым");
        }

        // 2. Создаём документ
        Document document = Document.createDraft(command.xmlContent(), command.companyId());
        log.info("Created document draft with ID: {}", document.getId());

        // 3. Сохраняем документ
        Document savedDocument = documentRepository.save(document);
        log.info("Saved document with ID: {}", savedDocument.getId());

        // 4. Трансформируем документ в XML
        log.info("Calling XML transformation for document ID: {}", savedDocument.getId());
        String xml = xmlTransformationPort.transformToXml(savedDocument);
        log.info("Transformed document to XML: {}", xml);

        // 5. Публикуем событие
        eventPublisher.publishDocumentCreated(
                new DocumentCreatedEvent(
                        savedDocument.getId(),
                        savedDocument.getCompanyId(),
                        savedDocument.getStatus(),
                        savedDocument.getCreatedAt()
                )
        );
        log.info("Published DocumentCreatedEvent for ID: {}", savedDocument.getId());

        // 6. Увеличиваем счётчик метрики
        documentCreatedCounter.increment();
        log.info("Metrics: documents.created = {}", documentCreatedCounter.count());

        // 7. Возвращаем ID
        return savedDocument.getId();
    }
}
