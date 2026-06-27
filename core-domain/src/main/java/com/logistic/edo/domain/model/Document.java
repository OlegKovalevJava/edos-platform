package com.logistic.edo.domain.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Агрегат "Электронный документ".
 *
 * <p>Это центральная сущность в системе ЭДО. Она инкапсулирует бизнес-логику
 * жизненного цикла документа и гарантирует целостность данных.</p>
 *
 * <p>Следуя принципам DDD, этот класс не содержит зависимостей от фреймворков
 * (Spring, Hibernate) и инфраструктуры (БД, Kafka).</p>
 */
public final class Document {

    private final DocumentId id;
    private DocumentStatus status;
    private final String xmlContent; // Содержимое документа (XML)
    private final String companyId;  // Идентификатор организации-владельца
    private final Instant createdAt; // Время создания
    private Instant updatedAt;       // Время последнего обновления

    // === Приватный конструктор для фабрик ===
    private Document(DocumentId id, DocumentStatus status, String xmlContent, String companyId, Instant createdAt,
                     Instant updatedAt) {
        this.id = Objects.requireNonNull(id, "DocumentId не может быть null");
        this.status = Objects.requireNonNull(status, "Статус не может быть null");
        this.xmlContent = Objects.requireNonNull(xmlContent, "Содержимое документа не может быть null");
        this.companyId = Objects.requireNonNull(companyId, "CompanyId не может быть null");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt не может быть null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt не может быть null");
    }

    // === Фабричные методы (единственный способ создания) ===
    /**
     * Создает новый документ в статусе DRAFT.
     */
    public static Document createDraft(String xmlContent, String companyId) {
        DocumentId newId = DocumentId.newId();
        Instant now = Instant.now();
        return new Document(newId, DocumentStatus.DRAFT, xmlContent, companyId, now, now);
    }

    /**
     * Восстанавливает документ из БД (для репозитория).
     * Используется только инфраструктурным слоем.
     */
    public static Document reconstitute(DocumentId id, DocumentStatus status, String xmlContent, String companyId,
                                        Instant createdAt, Instant updatedAt) {
        return new Document(id, status, xmlContent, companyId, createdAt, updatedAt);
    }

    // === Бизнес-методы (поведение агрегата) ===
    /**
     * Отправляет документ контрагенту.
     * Доступно только из статуса DRAFT или CREATED.
     */
    public void send() {
        if (this.status != DocumentStatus.DRAFT && this.status != DocumentStatus.CREATED) {
            throw new IllegalStateException(String.format("Нельзя отправить документ в статусе %s", this.status));
        }
        this.status = DocumentStatus.SENT;
        this.updatedAt = Instant.now();
    }

    /**
     * Подписывает документ со стороны текущего участника.
     * Доступно только из статуса SENT или DELIVERED.
     */
    public void sign() {
        if (this.status != DocumentStatus.SENT && this.status != DocumentStatus.DELIVERED) {
            throw new IllegalStateException(String.format("Нельзя подписать документ в статусе %s", this.status));
        }
        this.status = DocumentStatus.SIGNED;
        this.updatedAt = Instant.now();
    }

    /**
     * Отклоняет документ.
     * Доступно только из статуса SENT или DELIVERED.
     */
    public void reject() {
        if (this.status != DocumentStatus.SENT && this.status != DocumentStatus.DELIVERED) {
            throw new IllegalStateException(String.format("Нельзя отклонить документ в статусе %s", this.status));
        }
        this.status = DocumentStatus.REJECTED;
        this.updatedAt = Instant.now();
    }

    /**
     * Отмечает документ как доставленный до контрагента.
     * Доступно только из статуса SENT.
     */
    public void deliver() {
        if (this.status != DocumentStatus.SENT) {
            throw new IllegalStateException(String.format("Нельзя подтвердить доставку документа в статусе %s",
                    this.status));
        }
        this.status = DocumentStatus.DELIVERED;
        this.updatedAt = Instant.now();
    }

    // === Геттеры ===
    // ВАЖНО: геттеры нужны, но они только для чтения. Изменение полей происходит ТОЛЬКО через бизнес-методы.
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

    // === equals/hashCode ===
    // Агрегаты идентифицируются по идентификатору, а не по всем полям.
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
