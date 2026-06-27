package com.logistic.edo.application.command;

/**
 * Команда для создания нового документа в системе ЭДО.
 * Содержит все данные, необходимые для создания документа.
 */
public record CreateDocumentCommand(String xmlContent, String companyId) {
}
