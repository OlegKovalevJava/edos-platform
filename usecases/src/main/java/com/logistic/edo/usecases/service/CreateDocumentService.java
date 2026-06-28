package com.logistic.edo.usecases.service;

import com.logistic.edo.domain.event.DocumentCreatedEvent;
import com.logistic.edo.domain.model.Document;
import com.logistic.edo.domain.model.DocumentId;
import com.logistic.edo.domain.port.DocumentRepositoryPort;
import com.logistic.edo.domain.port.DocumentEventPublisherPort;
import com.logistic.edo.usecases.command.CreateDocumentCommand;
import com.logistic.edo.usecases.port.in.CreateDocumentUseCase;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CreateDocumentService implements CreateDocumentUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateDocumentService.class);

    private final DocumentRepositoryPort documentRepository;
    private final DocumentEventPublisherPort eventPublisher;

    public CreateDocumentService(DocumentRepositoryPort documentRepository,
                                 DocumentEventPublisherPort eventPublisher) {
        this.documentRepository = documentRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public DocumentId execute(CreateDocumentCommand command) {
        // 1. Валидация входных данных
        if (command.xmlContent() == null || command.xmlContent().isBlank()) {
            throw new IllegalArgumentException("Содержимое документа не может быть пустым");
        }
        if (command.companyId() == null || command.companyId().isBlank()) {
            throw new IllegalArgumentException("Идентификатор компании не может быть пустым");
        }

        // 2. Создаём документ через фабрику домена
        Document document = Document.createDraft(command.xmlContent(), command.companyId());
        log.info("Created document draft with ID: {}", document.getId());

        // 3. Сохраняем документ через порт репозитория
        Document savedDocument = documentRepository.save(document);
        log.info("Saved document with ID: {}", savedDocument.getId());

        // 4. Публикуем событие через порт
        eventPublisher.publishDocumentCreated(
                new DocumentCreatedEvent(
                        savedDocument.getId(),
                        savedDocument.getCompanyId(),
                        savedDocument.getStatus(),
                        savedDocument.getCreatedAt()
                )
        );
        log.info("Published DocumentCreatedEvent for ID: {}", savedDocument.getId());

        // 5. Возвращаем ID созданного документа
        return savedDocument.getId();
    }
}