package com.logistic.edo.domain.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Агрегат "Электронный документ".
 * Это центральная сущность в системе ЭДО. Она инкапсулирует бизнес-логику
 * жизненного цикла документа и гарантирует целостность данных.</p>
 * Следуя принципам DDD, этот класс не содержит зависимостей от фреймворков
 * (Spring, Hibernate) и инфраструктуры (БД, Kafka).</p>
 */
public final class Document {

    private final DocumentId id;
    private DocumentStatus status;
    private final String xmlContent;
    private final String companyId;
    private final Instant createdAt;
    private Instant updatedAt;

    private Document(DocumentId id, DocumentStatus status, String xmlContent, String companyId, Instant createdAt,
                     Instant updatedAt) {
        this.id = Objects.requireNonNull(id, "DocumentId не может быть null");
        this.status = Objects.requireNonNull(status, "Статус не может быть null");
        this.xmlContent = Objects.requireNonNull(xmlContent, "Содержимое документа не может быть null");
        this.companyId = Objects.requireNonNull(companyId, "CompanyId не может быть null");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt не может быть null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt не может быть null");
    }

    public static Document createDraft(String xmlContent, String companyId) {
        DocumentId newId = DocumentId.newId();
        Instant now = Instant.now();
        return new Document(newId, DocumentStatus.DRAFT, xmlContent, companyId, now, now);
    }

    public static Document reconstitute(DocumentId id, DocumentStatus status, String xmlContent, String companyId,
                                        Instant createdAt, Instant updatedAt) {
        return new Document(id, status, xmlContent, companyId, createdAt, updatedAt);
    }

    public void send() {
        if (this.status != DocumentStatus.DRAFT && this.status != DocumentStatus.CREATED) {
            throw new IllegalStateException(String.format("Нельзя отправить документ в статусе %s", this.status));
        }
        this.status = DocumentStatus.SENT;
        this.updatedAt = Instant.now();
    }

    public void sign() {
        if (this.status != DocumentStatus.SENT && this.status != DocumentStatus.DELIVERED) {
            throw new IllegalStateException(String.format("Нельзя подписать документ в статусе %s", this.status));
        }
        this.status = DocumentStatus.SIGNED;
        this.updatedAt = Instant.now();
    }

    public void reject() {
        if (this.status != DocumentStatus.SENT && this.status != DocumentStatus.DELIVERED) {
            throw new IllegalStateException(String.format("Нельзя отклонить документ в статусе %s", this.status));
        }
        this.status = DocumentStatus.REJECTED;
        this.updatedAt = Instant.now();
    }

    public void deliver() {
        if (this.status != DocumentStatus.SENT) {
            throw new IllegalStateException(String.format("Нельзя подтвердить доставку документа в статусе %s",
                    this.status));
        }
        this.status = DocumentStatus.DELIVERED;
        this.updatedAt = Instant.now();
    }

    public DocumentId getId() {
        return id;
    }

    public DocumentStatus getStatus() {
        return status;
    }

    public String getXmlContent() {
        return xmlContent;
    }

    public String getCompanyId() {
        return companyId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Document document = (Document) o;
        return Objects.equals(id, document.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
