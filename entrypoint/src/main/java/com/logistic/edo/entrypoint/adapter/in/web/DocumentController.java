package com.logistic.edo.entrypoint.adapter.in.web;

import com.logistic.edo.api.dto.CreateDocumentRequest;
import com.logistic.edo.api.dto.CreateDocumentResponse;
import com.logistic.edo.domain.model.DocumentId;  // ← Добавляем импорт DocumentId
import com.logistic.edo.usecases.command.CreateDocumentCommand;
import com.logistic.edo.usecases.port.in.CreateDocumentUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {

    private final CreateDocumentUseCase createDocumentUseCase;

    public DocumentController(CreateDocumentUseCase createDocumentUseCase) {
        this.createDocumentUseCase = createDocumentUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateDocumentResponse createDocument(@RequestBody CreateDocumentRequest request) {
        // 1. Создаём команду из запроса
        CreateDocumentCommand command = new CreateDocumentCommand(
                request.xmlContent(),
                request.companyId()
        );

        // 2. Выполняем Use Case и получаем DocumentId
        DocumentId documentId = createDocumentUseCase.execute(command);

        // 3. Возвращаем ответ с ID
        return new CreateDocumentResponse(documentId.value().toString());
    }
}
