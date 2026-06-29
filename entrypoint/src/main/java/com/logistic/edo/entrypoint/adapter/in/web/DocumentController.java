package com.logistic.edo.entrypoint.adapter.in.web;

import com.logistic.edo.api.dto.CreateDocumentRequest;
import com.logistic.edo.api.dto.CreateDocumentResponse;
import com.logistic.edo.domain.model.DocumentId;
import com.logistic.edo.usecases.command.CreateDocumentCommand;
import com.logistic.edo.usecases.port.in.CreateDocumentUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
        CreateDocumentCommand command = new CreateDocumentCommand(request.getXmlContent(), request.getCompanyId());
        DocumentId documentId = createDocumentUseCase.execute(command);
        CreateDocumentResponse response = new CreateDocumentResponse();
        response.setDocumentId(documentId.value().toString());
        return response;
    }
}
