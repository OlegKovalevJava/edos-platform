package com.logistic.edo.application.service;

import com.logistic.edo.application.command.CreateDocumentCommand;
import com.logistic.edo.application.port.in.CreateDocumentUseCase;
import com.logistic.edo.domain.model.Document;

/**
 * Реализация сценария "Создать документ".
 * Содержит бизнес-логику создания документа.
 */
public class CreateDocumentService implements CreateDocumentUseCase {

    @Override
    public Document execute(CreateDocumentCommand command) {
        // Валидация входных данных
        if (command.xmlContent() == null || command.xmlContent().isBlank()) {
            throw new IllegalArgumentException("Содержимое документа не может быть пустым");
        }
        if (command.companyId() == null || command.companyId().isBlank()) {
            throw new IllegalArgumentException("Идентификатор компании не может быть пустым");
        }

        // Используем фабрику из домена для создания документа
        return Document.createDraft(command.xmlContent(), command.companyId());
    }
}
