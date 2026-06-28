package com.logistic.edo.entrypoint.adapter.out.persistence;

import com.logistic.edo.domain.model.Document;
import com.logistic.edo.domain.model.DocumentId;
import com.logistic.edo.domain.model.DocumentStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentEntityMapper {

    @Mapping(target = "id", expression = "java(document.getId().value())")
    @Mapping(target = "status", expression = "java(document.getStatus().name())")
    @Mapping(target = "xmlContent", source = "xmlContent")
    @Mapping(target = "companyId", source = "companyId")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    DocumentEntity toEntity(Document document);

    default Document toDomain(DocumentEntity entity) {
        if (entity == null) {
            return null;
        }

        return Document.reconstitute(
                new DocumentId(entity.getId()),
                DocumentStatus.valueOf(entity.getStatus()),
                entity.getXmlContent(),
                entity.getCompanyId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
