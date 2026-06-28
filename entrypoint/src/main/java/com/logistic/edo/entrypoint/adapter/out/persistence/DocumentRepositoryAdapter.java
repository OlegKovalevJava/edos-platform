package com.logistic.edo.entrypoint.adapter.out.persistence;

import com.logistic.edo.domain.model.Document;
import com.logistic.edo.domain.model.DocumentId;
import com.logistic.edo.domain.port.DocumentRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DocumentRepositoryAdapter implements DocumentRepositoryPort {

    private final DocumentJpaRepository jpaRepository;
    private final DocumentEntityMapper mapper;

    @Override
    public Document save(Document document) {
        DocumentEntity entity = mapper.toEntity(document);
        DocumentEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Document> findById(DocumentId id) {
        return jpaRepository.findById(id.value()).map(mapper::toDomain);
    }
}
