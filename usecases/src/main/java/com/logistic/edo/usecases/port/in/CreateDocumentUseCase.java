package com.logistic.edo.usecases.port.in;

import com.logistic.edo.domain.model.DocumentId;
import com.logistic.edo.usecases.command.CreateDocumentCommand;

public interface CreateDocumentUseCase {
    DocumentId execute(CreateDocumentCommand command); // Возвращаем DocumentId
}