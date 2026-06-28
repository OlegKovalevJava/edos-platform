package com.logistic.edo.usecases.service;

import com.logistic.edo.domain.event.DocumentCreatedEvent;
import com.logistic.edo.domain.model.Document;
import com.logistic.edo.domain.model.DocumentId;
import com.logistic.edo.domain.port.DocumentRepositoryPort;
import com.logistic.edo.usecases.command.CreateDocumentCommand;
import com.logistic.edo.usecases.port.in.CreateDocumentUseCase;
import com.logistic.edo.usecases.port.out.DocumentEventPublisherPort;
import org.springframework.stereotype.Service;

@Service
public class CreateDocumentService implements CreateDocumentUseCase {

    private final DocumentRepositoryPort documentRepository;
    private final DocumentEventPublisherPort eventPublisher;

    // Явный конструктор вместо @RequiredArgsConstructor
    public CreateDocumentService(DocumentRepositoryPort documentRepository,
                                 DocumentEventPublisherPort eventPublisher) {
        this.documentRepository = documentRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public DocumentId execute(CreateDocumentCommand command) {
        // Валидация
        if (command.xmlContent() == null || command.xmlContent().isBlank()) {
            throw new IllegalArgumentException("Содержимое документа не может быть пустым");
        }
        if (command.companyId() == null || command.companyId().isBlank()) {
            throw new IllegalArgumentException("Идентификатор компании не может быть пустым");
        }

        // Создаём документ через фабрику домена
        Document document = Document.createDraft(command.xmlContent(), command.companyId());

        // Сохраняем через репозиторий (порт)
        documentRepository.save(document);

        // Публикуем событие через порт
        eventPublisher.publishDocumentCreated(
                new DocumentCreatedEvent(
                        document.getId(),
                        document.getCompanyId(),
                        document.getStatus(),
                        document.getCreatedAt()
                )
        );

        return document.getId();
    }
}