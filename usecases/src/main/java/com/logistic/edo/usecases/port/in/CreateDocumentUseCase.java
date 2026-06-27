package com.logistic.edo.usecases.port.in;

import com.logistic.edo.usecases.command.CreateDocumentCommand;
import com.logistic.edo.domain.model.Document;

/**
 * Интерфейс (порт) для сценария "Создать документ".
 * Определяет, какие входные данные нужны и что возвращается.
 */
public interface CreateDocumentUseCase {
    Document execute(CreateDocumentCommand command);
}
